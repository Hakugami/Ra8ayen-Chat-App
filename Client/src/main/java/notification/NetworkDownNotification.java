package notification;


@FunctionalInterface
public interface NetworkDownNotification<T> {
    T onNetworkDown() throws Exception;
}
