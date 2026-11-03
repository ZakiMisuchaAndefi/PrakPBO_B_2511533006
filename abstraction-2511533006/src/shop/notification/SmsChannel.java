package shop.notification;

public class SmsChannel extends AbstractChannel {
    private final int maxLength;

    public SmsChannel(int maxLength) {
        if (maxLength < 3) {
            throw new IllegalArgumentException("Maximum SMS length must be at least 3");
        }
        this.maxLength = maxLength;
    }

    @Override
    public String channelName() {
        return "SMS";
    }

    @Override
    protected String resolveRecipient(CustomerContact contact) {
        String phone = contact.getPhone();
        return phone.startsWith("08") ? phone : null;
    }

    @Override
    protected String prepare(String message) {
        if (message.length() <= maxLength) {
            return message;
        }
        return message.substring(0, maxLength - 3) + "...";
    }

    @Override
    protected boolean deliver(String recipient, String message) {
        System.out.println("[SMS to " + recipient + "] " + message);
        return true;
    }
}
