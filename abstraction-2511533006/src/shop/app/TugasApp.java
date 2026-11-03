package shop.app;

import java.util.List;
import shop.StudentInfo;
import shop.model.DigitalProduct;
import shop.model.PhysicalProduct;
import shop.model.Product;
import shop.notification.CustomerContact;
import shop.notification.EmailChannel;
import shop.notification.NotificationChannel;
import shop.notification.NotificationService;
import shop.notification.SmsChannel;
import shop.notification.WhatsAppChannel;
import shop.payment.BankTransferPayment;
import shop.payment.CreditCardPayment;
import shop.payment.PaymentMethod;
import shop.payment.EWalletPayment;
import shop.service.CheckoutService;
import shop.shipping.ExpressShipping;
import shop.shipping.ShippingService;

public class TugasApp {
    public static void main(String[] args) {
        Product coffee = new PhysicalProduct(
                "KOPI-001", "Kopi Arabika Gayo 250g", 85_000, 300);
        Product rendang = new PhysicalProduct(
                "RDG-002", "Rendang Kemasan 250g", 120_000, 400);
        Product ebook = new DigitalProduct(
                "EBK-003", "E-book Resep Kue Nusantara", 50_000,
                "https://toko-umkm.example/unduh/ebk-003");
        List<Product> cart = List.of(coffee, rendang, ebook);

        CheckoutService checkout = new CheckoutService();
        ShippingService express = new ExpressShipping();
        EWalletPayment wallet = new EWalletPayment("DompetKu", 500_000);
        PaymentMethod transfer = new BankTransferPayment("Bank Nagari");
        CreditCardPayment card = new CreditCardPayment("Siti Rahma", 1_000_000);

        System.out.println("Tugas oleh " + StudentInfo.NAME + " (" + StudentInfo.NIM + ")");
        System.out.println("\n== T1: Total dengan biaya admin (Ekspres) ==");
        printTotal(checkout, cart, express, wallet);
        printTotal(checkout, cart, express, transfer);
        printTotal(checkout, cart, express, card);
        System.out.println("\nKebijakan kartu kredit : " + card.policy());
        System.out.println("Kebijakan e-wallet   : " + wallet.policy());

        NotificationService notifications = new NotificationService();
        notifications.register(new EmailChannel());
        notifications.register(new SmsChannel(StudentInfo.SMS_MAX_LENGTH));
        notifications.register(new WhatsAppChannel());
        notifications.register(new NotificationChannel() {
            @Override
            public String channelName() {
                return "LOG";
            }

            @Override
            public boolean send(CustomerContact contact, String message) {
                if (contact == null || message == null || message.isBlank()) {
                    return false;
                }
                System.out.println("[LOG] " + contact.getName() + ": " + message);
                return true;
            }
        });

        String message = "Pesanan ORD-0001 sudah dikirim dengan kurir Ekspres. Nomor resi akan "
                + "muncul di aplikasi dalam 1x24 jam. Terima kasih telah berbelanja produk UMKM "
                + "Sumatera Barat di toko kami! Silakan hubungi layanan pelanggan jika membutuhkan bantuan.";
        CustomerContact siti = new CustomerContact("Siti Rahma", "siti@example.com", "081234567890");
        CustomerContact budi = new CustomerContact("Budi", "", "+6281298765432");
        System.out.println("\n== T2: Notifikasi status pesanan ==");
        System.out.println("Terkirim ke Siti lewat " + notifications.broadcast(siti, message) + " kanal");
        System.out.println("Terkirim ke Budi lewat " + notifications.broadcast(budi, message) + " kanal");
    }

    private static void printTotal(
            CheckoutService checkout, List<Product> cart, ShippingService shipping, PaymentMethod payment) {
        long total = checkout.grandTotal(cart, shipping, payment);
        long base = checkout.total(cart, shipping);
        System.out.println(payment.getName() + " : Rp" + total + " (admin Rp"
                + payment.adminFee(base) + ")");
    }
}
