package fr.lordhydra.mmr.error;


public class Result {

    private static enum State {OK, ERROR};

    private State state;
    private String message;

    private Result(State state, String message) {
        this.state = state;
        this.message = message;
    }

    public static Result ok() {
        return new Result(State.OK, null);
    }

    public static Result ok(String message) {
        return new Result(State.OK, message);
    }

    public static Result error(String message) {
        return new Result(State.ERROR, message);
    }

    public boolean hasMessage() {
        return (this.message != null);
    }

    public boolean isOk() {
        return state == State.OK;
    }

    public String getMessage() {
        return message;
    }


}


