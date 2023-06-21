package com.andree.panjaitan.parkeebe.order;

import com.andree.panjaitan.parkeebe.location.Location;
import com.andree.panjaitan.parkeebe.location.LocationCapacityRepository;
import com.andree.panjaitan.parkeebe.location.LocationRepository;
import com.andree.panjaitan.parkeebe.payment.Payment;
import com.andree.panjaitan.parkeebe.payment.PaymentMethod;
import com.andree.panjaitan.parkeebe.payment.PaymentRepository;
import com.andree.panjaitan.parkeebe.shared.CodeError;
import com.andree.panjaitan.parkeebe.shared.ErrorAppException;
import com.andree.panjaitan.parkeebe.user.User;
import com.andree.panjaitan.parkeebe.utils.QRCodeGenerator;
import com.andree.panjaitan.parkeebe.vehicle.Vehicle;
import com.andree.panjaitan.parkeebe.vehicle.VehicleRepository;
import com.andree.panjaitan.parkeebe.vehicle.VehicleTypeRepository;
import com.google.zxing.WriterException;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository repository;
    private final LocationRepository locationRepository;
    private final VehicleTypeRepository vehicleTypeRepository;
    private final VehicleRepository vehicleRepository;
    private final LocationCapacityRepository locationCapacityRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    @Value("${application.upload.path}")
    private String uploadPath;

    private final QRCodeGenerator qrCodeGenerator;

    @Transactional
    public ResponseRequestOrder requestOrder(RequestOrder request, User user) throws DocumentException, IOException, WriterException {
        var locationID = UUID
                .fromString(request.getLocationID());
        var vehicleTypeId = UUID
                .fromString(request.getVehicleTypeID());
        var check = orderRepository.findByVehiclePlatNumberAndExitAtNull(request.getPlatNumber()).orElse(null);
        if (check != null) {
            throw new ErrorAppException(CodeError.BAD_REQUEST.getCodeError(), "car already parked");
        }

        var location = locationRepository.findById(locationID)
                .orElseThrow(() -> new ErrorAppException(
                        CodeError.NOT_FOUND.getCodeError(), "location not found"));

        var locationCapacity = locationCapacityRepository
                .findByVehicleTypeIdAndLocationIdAndDeletedAtIsNull(
                        vehicleTypeId,
                        locationID
                ).orElseThrow(() -> new ErrorAppException(
                        CodeError.NOT_FOUND.getCodeError(), "vehicle not found "));

        var detail = locationRepository.countUsedLocationByVehicleTypeID(
                        locationID,
                        vehicleTypeId)
                .orElseThrow(() -> new ErrorAppException(
                        CodeError.NOT_FOUND.getCodeError(), "detail not found"));

        if (detail.getAvailable_capacity() == 0) {
            throw new ErrorAppException(
                    CodeError.BAD_REQUEST_PARK_IS_FULL.getCodeError(), "park is full");
        }

        var vehicle = vehicleRepository.findById(request.getPlatNumber()).orElse(
                Vehicle
                        .builder()
                        .vehicleType(locationCapacity.getVehicleType())
                        .totalParking(0)
                        .platNumber(request.getPlatNumber())
                        .build()
        );

        vehicle = vehicleRepository.save(vehicle);
        var code = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 8)
                .toUpperCase();
        var order = Order
                .builder()
                .guard(user)
                .uniqueCode(code)
                .entryAt(LocalDateTime.now())
                .entryCarImage(request.getFrontImage())
                .exitCarDriverImage(request.getDriverImage())
                .location(location)
                .vehicle(vehicle)
                .pricePerHour(locationCapacity.getPricePerHour())
                .build();
        order.setCreatedBy(user.getUsername());
        order = repository.save(order);

        String linkTicket = createTicket(
                code,
                request,
                location,
                order,
                locationCapacity.getPricePerHour(),
                vehicle);

        order.setTicketLink(linkTicket);
        repository.save(order);

        return ResponseRequestOrder.builder()
                .linkParkingTicket(linkTicket)
                .build();
    }

    private String createTicket(
            String code,
            RequestOrder request,
            Location location,
            Order order,
            BigInteger pricePerHour,
            Vehicle vehicle) throws IOException, WriterException, DocumentException {
        String pathFile = uploadPath;
        Path path = Paths.get(pathFile);
        Files.createDirectories(path);
        String formatNormal = vehicle
                .getPlatNumber().replace(" ", "")
                + "_" + order.getId().toString()
                .replace("-", "");
        String pathBarCode = pathFile + "/" + formatNormal + ".png";
        qrCodeGenerator.generateQRCodeImage(
                code,
                150,
                150,
                pathBarCode
        );
        String docPath = pathFile + "/" + formatNormal + ".pdf";
        Document document = new Document(new Rectangle(250, 400));
        PdfWriter.getInstance(document, new FileOutputStream(docPath));
        document.open();

        Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
        Font fontDetail = FontFactory.getFont(FontFactory.COURIER, 7, BaseColor.BLACK);

        // Add title
        Chunk titlePdf = new Chunk("Ticket Parking", font);
        Paragraph paragraph = new Paragraph();
        paragraph.add(titlePdf);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        document.add(paragraph);

        var decFormat = new DecimalFormat("#,###");
        Chunk detailTime = new Chunk("\n\nTime entry : " + order.getEntryAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a")), fontDetail);
        Chunk platNumber = new Chunk("\nPlat Number : " + vehicle.getPlatNumber(), fontDetail);
        Chunk detailVehicle = new Chunk("\nVehicle Type : " + vehicle.getVehicleType().getName(), fontDetail);
        Chunk detailPrice = new Chunk("\nParking Price Per (Hour) Rp.%s".formatted(decFormat.format(pricePerHour)), fontDetail);
        Chunk parkingAddress = new Chunk("\nParking Address : %s".formatted(location.getAddress()), fontDetail);


        Phrase phrase = new Phrase();
        phrase.add(detailTime);
        phrase.add(platNumber);
        phrase.add(detailVehicle);
        phrase.add(detailPrice);
        phrase.add(parkingAddress);
        Paragraph parDetail = new Paragraph();
        parDetail.add(phrase);

        document.add(parDetail);

        Image image = Image.getInstance(pathBarCode);
        image.setAlignment(Element.ALIGN_CENTER);
        document.add(image);

        document.close();


        return "/public/" + formatNormal + ".pdf";
    }

    @Transactional
    public ResponseCheckout checkoutBarcode(RequestCheckout request, User user) {
        var order = orderRepository.findByUniqueCodeAndExitAtNull(request.getCode())
                .orElseThrow(() -> new ErrorAppException(CodeError.BAD_REQUEST.getCodeError(), "code is wrong"));
        return processCheckout(request, user, order);
    }

    public ResponseCheckout processCheckout(RequestCheckout request, User user, Order order) {
        var now = LocalDateTime.now();
        String platNumber = order.getVehicle().getPlatNumber();
        var vh = vehicleRepository.findById(platNumber).orElseThrow(
                () -> new ErrorAppException(CodeError.BAD_REQUEST.getCodeError(), "plat is not found")
        );

        Date entryAt = Timestamp.valueOf(order.getEntryAt());
        Date exitAt = Timestamp.valueOf(now);

        var totalDifferent = exitAt.getTime() - entryAt.getTime();
        double difInHour = (totalDifferent * 1.0) / 3600000.0;
        difInHour = Math.ceil(difInHour);
        int afterCeil = (int) difInHour;
        BigInteger totalPrice = order.getPricePerHour().multiply(BigInteger.valueOf(afterCeil));

        var difInDay = (totalDifferent / (1000 * 60 * 60 * 24)) % 365;
        var difInHourNew = (totalDifferent
                / (1000 * 60 * 60))
                % 24;
        var difInMinute = (totalDifferent
                / (1000 * 60))
                % 60;

        var difInSecond = (totalDifferent
                / 1000)
                % 60;
        var difString  = "%d Days %d Hour %d Minutes %d Seconds".formatted(difInDay, difInHourNew, difInMinute, difInSecond);
        order.setExitAt(now);
        order.setExitCarImage(request.getFrontPicture());
        order.setExitCarDriverImage(request.getDriverPicture());
        order.setTotalHour(afterCeil);
        order.setTotalPrice(totalPrice);
        order.setUpdatedBy(user.getUsername());
        order.setDifString(difString);
        order = orderRepository.save(order);

        var codePaid = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        var payment = Payment
                .builder()
                .originalPrice(totalPrice)
                .totalPrice(totalPrice)
                .order(order)
                .codePaid(codePaid)
                .build();

        payment = paymentRepository.save(payment);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-ddd HH:mm:ss a");
        return ResponseCheckout
                .builder()
                .vehicleType(vh.getVehicleType().getName())
                .codePayment(payment.getCodePaid())
                .entryTime(simpleDateFormat.format(entryAt))
                .exitTime(simpleDateFormat.format(exitAt))
                .totalDif(difString)
                .totalPrice(totalPrice)
                .build();
    }

    @Transactional
    public ResponseCheckout checkoutManual(RequestCheckout request, User user) {
        var order = orderRepository.findByVehiclePlatNumberAndExitAtNull(request.getCode())
                .orElseThrow(() -> new ErrorAppException(CodeError.BAD_REQUEST.getCodeError(), "code is wrong"));
        return processCheckout(request, user, order);
    }
}
