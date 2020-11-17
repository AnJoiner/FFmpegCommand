package com.coder.ffmpeg.exception;

import androidx.annotation.Nullable;

public class CmdExecuteException extends Throwable {

    private int code;

    public CmdExecuteException(int code) {
        this.code = code;
    }

    public CmdExecuteException(@Nullable String message, int code) {
        super(message);
        this.code = code;
    }

    public CmdExecuteException(@Nullable String message, @Nullable Throwable cause, int code) {
        super(message, cause);
        this.code = code;
    }

    public CmdExecuteException(@Nullable Throwable cause, int code) {
        super(cause);
        this.code = code;
    }

}
