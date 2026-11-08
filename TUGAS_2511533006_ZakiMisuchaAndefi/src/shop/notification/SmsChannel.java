package shop.notification;

public class SmsChannel extends AbstractChannel {
    private final int maxLength;

    public SmsChannel(int maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public String channelName() {
        return "SMS";
    }

    @Override
    protected String resolveRecipient(CustomerContact contact) {
        if (contact != null && contact.getPhone() != null && contact.getPhone().startsWith("08")) {
            return contact.getPhone();
        }
        return null;
    }

    @Override
    protected String prepare(String message) {
        if (message != null && message.length() > maxLength) {
            return message.substring(0, Math.max(0, maxLength - 3)) + "...";
        }
        return message;
    }

    @Override
    protected boolean deliver(String recipient, String message) {
        System.out.println("[SMS to " + recipient + "] " + message);
        return true;
    }
}
