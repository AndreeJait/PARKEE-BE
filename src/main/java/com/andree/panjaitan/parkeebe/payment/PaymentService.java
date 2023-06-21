package com.andree.panjaitan.parkeebe.payment;

import com.andree.panjaitan.parkeebe.order.OrderRepository;
import com.andree.panjaitan.parkeebe.shared.CodeError;
import com.andree.panjaitan.parkeebe.shared.ErrorAppException;
import com.andree.panjaitan.parkeebe.user.User;
import com.andree.panjaitan.parkeebe.voucher.VoucherRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository repository;
    private final PaymentVoucherRepository paymentVoucherRepository;
    private final VoucherRepository voucherRepository;
    private final OrderRepository orderRepository;

    @Value("${application.upload.path}")
    private String uploadPath;

    public Payment findPaymentByID(UUID uuid) {
        return repository.findById(uuid).orElseThrow(() -> new ErrorAppException(CodeError.NOT_FOUND.getCodeError(), "payment not found"));
    }

    public ResponsePaid paid(RequestPaid request, User user) throws DocumentException, FileNotFoundException {
        var payment = repository.findByCodePaid(
                request.getCodePayment()).orElseThrow(() ->
                new ErrorAppException(CodeError.NOT_FOUND.getCodeError(), "payment not found"));
        var order = orderRepository.findById(payment.getOrder().getId()).orElseThrow(
                () -> new ErrorAppException(CodeError.USER_NOT_FOUND.getCodeError(), "order not found")
        );

        if (request.getPaymentMethod().equalsIgnoreCase(PaymentMethod.CASH.getName())) {
            payment.setPaidAt(LocalDateTime.now());
            payment.setPaymentMethod(PaymentMethod.CASH);
            var originalValue = payment.getOriginalPrice();
            var totalPrice = originalValue;
            List<PaymentVoucher> paymentVouchers = new ArrayList<>();
            for (int i = 0; i < request.vouchers.size(); i++) {
                var voucher = voucherRepository.findByCodeVoucher(request.getVouchers().get(i))
                        .orElseThrow(() -> new ErrorAppException(
                                CodeError.BAD_REQUEST.getCodeError(), "voucher not found"));
                paymentVouchers.add(PaymentVoucher
                        .builder()
                        .payment(payment)
                        .voucher(voucher)
                        .build());
                if (!voucher.isPercentage()) {
                    totalPrice = originalValue.subtract(voucher.getValue());
                } else {
                    totalPrice = originalValue.subtract(originalValue.multiply(voucher.getValue()));
                }
            }
            paymentVoucherRepository.saveAll(paymentVouchers);

            payment.setTotalPrice(totalPrice);
            payment = repository.save(payment);
            order.setPaid(true);
            orderRepository.save(order);
            String structPaid = createStructPaid(payment);
            return ResponsePaid
                    .builder()
                    .structPaid(structPaid)
                    .build();
        }
        throw new ErrorAppException(CodeError.BAD_REQUEST.getCodeError(), "payment method not supported yet");
    }

    private String createStructPaid(Payment payment) throws FileNotFoundException, DocumentException {
        String formatNormal = payment.getId().toString()
                .replace("-", "");

        String docPath = uploadPath + "/" + formatNormal + ".pdf";
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

        Chunk detailTime = new Chunk("\n\nTime entry : " + payment.getOrder().getEntryAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a")), fontDetail);
        Chunk detailTimeExit = new Chunk("\nTime Exit : " + payment.getOrder().getExitAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a")), fontDetail);
        Chunk platNumber = new Chunk("\nPlat Number : " + payment.getOrder().getVehicle().getPlatNumber(), fontDetail);
        Chunk detailVehicle = new Chunk("\nVehicle Type : " + payment.getOrder().getVehicle().getVehicleType().getName(), fontDetail);
        Chunk originPrice = new Chunk("Origin Price : Rp.%s".formatted(decFormat.format(payment.getOriginalPrice())), fontDetail);
        Chunk totalPrice = new Chunk("\nTotal Price : Rp.%s".formatted(decFormat.format(payment.getTotalPrice())), fontDetail);
        Chunk paymentMethod = new Chunk("\nPayment Method : %s".formatted(payment.getPaymentMethod().getName()), fontDetail);
        Chunk duration = new Chunk("\nDuration : %s".formatted(payment.getOrder().getDifString()), fontDetail);


        Phrase phrase = new Phrase();

        phrase.add(detailTime);
        phrase.add(detailTimeExit);
        phrase.add(platNumber);
        phrase.add(detailVehicle);


        for (int i = 0; i < payment.getVouchers().size(); i++) {
            var v = payment.getVouchers().get(i);
            Chunk voucher = new Chunk("\nApply voucher : %s".formatted(v.getVoucher().getCodeVoucher()), fontDetail);
            int voucherValue = v.getVoucher().getValue().intValue();
            if (v.getVoucher().isPercentage()) {
                voucherValue = payment.getOriginalPrice().intValue() * (voucherValue / 100);
            }
            Chunk voucherValueText = new Chunk("\nVoucher Value : Rp.%s\n".formatted(decFormat.format(voucherValue)), fontDetail);
            phrase.add(voucher);
            phrase.add(voucherValueText);
        }

        phrase.add(originPrice);
        phrase.add(totalPrice);
        phrase.add(paymentMethod);
        phrase.add(duration);
        Paragraph parDetail = new Paragraph();
        parDetail.add(phrase);

        document.add(parDetail);

        document.close();
        return "/public/" + formatNormal + ".pdf";

    }

    public Payment findPaymentByCodePayment(String codePayment) {
        return repository.findByCodePaid(codePayment).orElseThrow(() -> new ErrorAppException(CodeError.NOT_FOUND.getCodeError(), "payment not found"));
    }
}
