package review.fixed;

import java.util.ArrayList;
import java.util.List;

/*
 * T3 - CODE REVIEW & REFACTORED
 * Versi refactor dari BadShop yang memperbaiki masalah abstraksi:
 * 1. Interface Segregation: Memisahkan StoreOperations yang fat menjadi PaymentMethod & Printable.
 * 2. Polymorphism: Menghilangkan switch-case string pada Courier dengan subclass polimorfis.
 * 3. Immutability: Mengubah Address menjadi immutable final class tanpa setter.
 * 4. Polimorfisme pada Discount: Menghilangkan pengecekan instanceof di default method interface.
 * 5. Static Nested Class: Mengubah Cart.Item menjadi static class.
 * 6. Mata Uang: Menggunakan long untuk saldo/uang bukan double.
 */

// 1. Interface segregation
interface PaymentMethod {
    boolean pay(long amount);
}

interface Printable {
    String printInvoice();
}

class GiftVoucher implements PaymentMethod, Printable {
    private long balance = 100_000L;

    @Override
    public boolean pay(long amount) {
        if (amount <= 0 || amount > balance) {
            return false;
        }
        balance -= amount;
        return true;
    }

    @Override
    public String printInvoice() {
        return "sisa Rp" + balance;
    }
}

// 2. Abstraksi Courier polimorfis (tanpa magic string switch-case)
abstract class Courier {
    private final String name;

    protected Courier(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    protected abstract long ratePerKg();

    public long cost(int kilograms) {
        return kilograms * ratePerKg();
    }
}

class ExpressCourier extends Courier {
    public ExpressCourier() {
        super("Ekspres");
    }

    @Override
    protected long ratePerKg() {
        return 18_000L;
    }
}

// 5. Static nested class untuk Cart & Item
class Cart {
    private final List<Item> items = new ArrayList<>();

    public static class Item {
        private final String name;
        private final long price;
        private final int quantity;

        public Item(String name, long price, int quantity) {
            this.name = name;
            this.price = price;
            this.quantity = quantity;
        }

        public long subtotal() {
            return price * quantity;
        }

        public String getName() {
            return name;
        }
    }

    public void add(Item item) {
        items.add(item);
    }

    public long total() {
        long total = 0;
        for (Item item : items) {
            total += item.subtotal();
        }
        return total;
    }
}

// 3. Immutable Address (final class, private final fields, tanpa setter)
final class Address {
    private final String street;
    private final String city;

    public Address(String street, String city) {
        this.street = street;
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    @Override
    public String toString() {
        return street + ", " + city;
    }
}

class SimpleOrder {
    private final String id;
    private final Address address;

    public SimpleOrder(String id, Address address) {
        this.id = id;
        this.address = address;
    }

    public Address getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return id + " -> " + address;
    }
}

// 4. Interface Discount dengan abstract method polimorfis
interface Discount {
    long apply(long price);
}

class MemberDiscount implements Discount {
    @Override
    public long apply(long price) {
        return price - price * 5 / 100;
    }
}

class FlashSaleDiscount implements Discount {
    @Override
    public long apply(long price) {
        return price - price * 30 / 100;
    }
}

public class BadShopRefactored {
    public static void main(String[] args) {
        Cart cart = new Cart();
        cart.add(new Cart.Item("Kopi Gayo", 85_000, 2));
        System.out.println("Total cart: " + cart.total());

        Courier courier = new ExpressCourier();
        System.out.println("Ongkir " + courier.getName() + " 2 kg: " + courier.cost(2));

        Address padang = new Address("Jl. Sudirman 10", "Padang");
        Address bukittinggi = new Address("Jl. Sudirman 10", "Bukittinggi");
        SimpleOrder first = new SimpleOrder("ORD-1", padang);
        SimpleOrder second = new SimpleOrder("ORD-2", bukittinggi);
        System.out.println(first);
        System.out.println(second);

        Discount flashSale = new FlashSaleDiscount();
        System.out.println("Harga flash sale: " + flashSale.apply(100_000));

        GiftVoucher voucher = new GiftVoucher();
        System.out.println(voucher.pay(30_000) + " | " + voucher.printInvoice());
    }
}
