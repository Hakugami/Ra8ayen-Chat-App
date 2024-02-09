package notification;

public interface NetworkDownNotification<T> {
    T onNetworkDown() throws Exception;
}
