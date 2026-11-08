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
import shop.payment.EWalletPayment;
import shop.service.CheckoutService;
import shop.shipping.ExpressShipping;
import shop.shipping.ShippingService;

public class TugasApp {
    public static void main(String[] args) {
        System.out.println("Tugas oleh " + StudentInfo.NAME + " (" + StudentInfo.NIM + ")");

        Product coffee = new PhysicalProduct("KOPI-001", "Kopi Arabika Gayo 250g", 85_000, 300);
        Product rendang = new PhysicalProduct("RDG-002", "Rendang Kemasan 250g", 120_000, 400);
        Product ebook = new DigitalProduct("EBK-003", "E-book Resep Kue Nusantara", 50_000,
                "https://toko-umkm.example/unduh/ebk-003");
        List<Product> cart = List.of(coffee, rendang, ebook);

        CheckoutService checkout = new CheckoutService();
        ShippingService express = new ExpressShipping();

        EWalletPayment wallet = new EWalletPayment("DompetKu", 300_000);
        BankTransferPayment transfer = new BankTransferPayment("Bank Nagari");
        CreditCardPayment card = new CreditCardPayment("Siti", 1_000_000);

        long baseTotal = checkout.total(cart, express);

        System.out.println("\n== T1: Total dengan biaya admin (Ekspres) ==");
        System.out.println(wallet.getName() + " : Rp" + checkout.grandTotal(cart, express, wallet)
                + " (admin Rp" + wallet.adminFee(baseTotal) + ")");
        System.out.println(transfer.getName() + " : Rp" + checkout.grandTotal(cart, express, transfer)
                + " (admin Rp" + transfer.adminFee(baseTotal) + ")");
        System.out.println(card.getName() + " : Rp" + checkout.grandTotal(cart, express, card)
                + " (admin Rp" + card.adminFee(baseTotal) + ")");

        System.out.println("\nKebijakan kartu kredit : " + card.policy());
        System.out.println("Kebijakan e-wallet     : " + wallet.policy());

        System.out.println("\n== T2: Notifikasi status pesanan ==");
        NotificationService service = new NotificationService();
        service.register(new EmailChannel());
        service.register(new SmsChannel(StudentInfo.SMS_MAX_LENGTH));
        service.register(new WhatsAppChannel());

        // Anonymous class untuk log konsol
        NotificationChannel logChannel = new NotificationChannel() {
            @Override
            public String channelName() {
                return "ConsoleLog";
            }

            @Override
            public boolean send(CustomerContact contact, String message) {
                if (contact == null || message == null || message.isBlank()) {
                    return false;
                }
                System.out.println("[LOG] " + contact.getName() + ": " + message);
                return true;
            }
        };
        service.register(logChannel);

        // Pesan panjang > 200 karakter
        String longMessage = "Pesanan ORD-0001 sudah dikirim dengan kurir Ekspres. "
                + "Nomor resi pengiriman akan segera diperbarui dan dapat dipantau di aplikasi dalam 1x24 jam ke depan. "
                + "Terima kasih banyak telah berbelanja produk UMKM Sumatera Barat di toko kami!";

        CustomerContact Siti = new CustomerContact("Siti", "Siti@example.com", "081234567890");
        int countSiti = service.broadcast(Siti, longMessage);
        System.out.println("Terkirim ke Siti lewat " + countSiti + " kanal");

        CustomerContact budi = new CustomerContact("Budi", null, "+6281298765432");
        int countBudi = service.broadcast(budi, longMessage);
        System.out.println("Terkirim ke Budi lewat " + countBudi + " kanal");
    }
}
