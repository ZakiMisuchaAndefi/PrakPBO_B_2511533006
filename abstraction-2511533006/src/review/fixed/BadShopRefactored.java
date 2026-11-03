package review.fixed;

import java.util.ArrayList;
import java.util.List;

interface Payable {
    boolean pay(long amount);
}

interface Shippable {
    long shippingCost(int weightGrams);
}

final class GiftVoucher implements Payable {
    private long balance = 100_000L;

    @Override
    public boolean pay(long amount) {
        if (amount <= 0 || amount > balance) {
            return false;
        }
        balance -= amount;
        return true;
    }

    public long getBalance() {
        return balance;
    }
}

abstract class Courier implements Shippable {
    protected abstract long ratePerKg();

    @Override
    public final long shippingCost(int weightGrams) {
        if (weightGrams <= 0) {
            return 0;
        }
        long kilograms = (weightGrams + 999L) / 1000;
        return kilograms * ratePerKg();
    }
}

final class ExpressCourier extends Courier {
    @Override
    protected long ratePerKg() {
        return 18_000L;
    }
}

final class Cart {
    private final List<Item> items = new ArrayList<>();

    public void add(String name, long price, int quantity) {
        items.add(new Item(name, price, quantity));
    }

    public long total() {
        long total = 0;
        for (Item item : items) {
            total += item.subtotal();
        }
        return total;
    }

    private static final class Item {
        private final String name;
        private final long price;
        private final int quantity;

        private Item(String name, long price, int quantity) {
            if (name == null || name.trim().isEmpty() || price < 0 || quantity <= 0) {
                throw new IllegalArgumentException("Invalid cart item");
            }
            this.name = name;
            this.price = price;
            this.quantity = quantity;
        }

        private long subtotal() {
            return price * quantity;
        }
    }
}

final class Address {
    private final String street;
    private final String city;

    Address(String street, String city) {
        if (street == null || street.trim().isEmpty()
                || city == null || city.trim().isEmpty()) {
            throw new IllegalArgumentException("Street and city are required");
        }
        this.street = street;
        this.city = city;
    }

    @Override
    public String toString() {
        return street + ", " + city;
    }
}

final class SimpleOrder {
    private final String id;
    private final Address address;

    SimpleOrder(String id, Address address) {
        if (id == null || id.trim().isEmpty() || address == null) {
            throw new IllegalArgumentException("Order id and address are required");
        }
        this.id = id;
        this.address = address;
    }

    @Override
    public String toString() {
        return id + " -> " + address;
    }
}

interface Discount {
    int percent();

    default long apply(long price) {
        int percent = Math.max(0, Math.min(percent(), 100));
        return price - price * percent / 100;
    }
}

final class FlashSaleDiscount implements Discount {
    @Override
    public int percent() {
        return 30;
    }
}

public class BadShopRefactored {
    public static void main(String[] args) {
        Cart cart = new Cart();
        cart.add("Kopi Gayo", 85_000, 2);
        System.out.println("Total cart: " + cart.total());

        Shippable courier = new ExpressCourier();
        System.out.println("Ongkir EKSPRES 2 kg: " + courier.shippingCost(2_000));

        Address padang = new Address("Jl. Sudirman 10", "Padang");
        Address bukittinggi = new Address("Jl. Sudirman 10", "Bukittinggi");
        System.out.println(new SimpleOrder("ORD-1", padang));
        System.out.println(new SimpleOrder("ORD-2", bukittinggi));

        Discount flashSale = new FlashSaleDiscount();
        System.out.println("Harga flash sale: " + flashSale.apply(100_000));

        GiftVoucher voucher = new GiftVoucher();
        Payable payment = voucher;
        System.out.println(payment.pay(30_000) + " | sisa Rp" + voucher.getBalance());
    }
}
