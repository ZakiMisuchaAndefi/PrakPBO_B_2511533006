package shop.notification;

public class EmailChannel extends AbstractChannel {
    public EmailChannel() {
    }

    @Override
    public String channelName() {
        return "Email";
    }

    @Override
    protected String resolveRecipient(CustomerContact contact) {
        if (contact != null && contact.getEmail() != null && contact.getEmail().contains("@")) {
            return contact.getEmail();
        }
        return null;
    }

    @Override
    protected boolean deliver(String recipient, String message) {
        System.out.println("[EMAIL to " + recipient + "] " + message);
        return true;
    }
}
