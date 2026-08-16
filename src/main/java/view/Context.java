package view;

/**
 * Contenitore leggero usato dal Navigator per trasportare dati da una
 * schermata all'altra durante la navigazione (es. il ruolo scelto nella
 * schermata di selezione ruolo, oppure la SessionBean ottenuta dopo il login).
 */
public class Context {

    private final Object data;

    public Context() {
        this(null);
    }

    public Context(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    /**
     * Restituisce il dato trasportato, se compatibile con il tipo richiesto,
     * altrimenti null.
     */
    public <T> T getData(Class<T> type) {
        return (data != null && type.isInstance(data)) ? type.cast(data) : null;
    }
}
