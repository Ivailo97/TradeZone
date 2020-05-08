package TradeZone.service.validation;

public interface ValidationService<T> {

    boolean isValid(T element);
}
