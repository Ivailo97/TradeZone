package TradeZone.data.model.service.validation;

public interface ValidationService<T> {

    boolean isValid(T element);
}
