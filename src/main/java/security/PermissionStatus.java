package security;

public enum PermissionStatus {

    OK(200),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404);

    int code;

    PermissionStatus(int code) {
        this.code = code;
    }
}
