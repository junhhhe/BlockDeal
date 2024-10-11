package SenierProject.BlockDeal.exception;

import SenierProject.BlockDeal.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 로그인 실패 예외 처리
    @ExceptionHandler(LoginFailedException.class)
    public ResponseEntity<ApiResponse<String>> handleLoginFailedException(LoginFailedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse<>(false, ex.getMessage(), null));
    }

    // MemberNotFoundException 예외 처리
    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleMemberNotFoundException(MemberNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, ex.getMessage(), null));
    }

    // 상품 찾기 실패 예외 처리
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleProductNotFoundException(ProductNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, ex.getMessage(), null));
    }

    // 찜 목록에 이미 존재하는 상품 예외 처리
    @ExceptionHandler(WishlistAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<String>> handleWishlistAlreadyExistsException(WishlistAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiResponse<>(false, ex.getMessage(), null));
    }

    // 사용자가 자신의 상품을 찜하려고 할 때 예외 처리
    @ExceptionHandler(OwnProductWishlistException.class)
    public ResponseEntity<ApiResponse<String>> handleOwnProductWishlistException(OwnProductWishlistException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, ex.getMessage(), null));
    }

    //채팅 예외 처리
    @ExceptionHandler(ChatRoomNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleChatRoomNotFoundException(ChatRoomNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, ex.getMessage(), null));
    }

    @ExceptionHandler(ChatRoomAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<String>> handleChatRoomAlreadyExistsException(ChatRoomAlreadyExistsException ex) {
        ApiResponse<String> response = new ApiResponse<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    // 메시지 전송 시 예외 처리
    @ExceptionHandler(MessageSendException.class)
    public ResponseEntity<ApiResponse<String>> handleMessageSendException(MessageSendException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, ex.getMessage(), null));
    }

    // 권한이 없는 사용자 접근 시 예외 처리
    @ExceptionHandler(IllegalAccessException.class)
    public ResponseEntity<ApiResponse<String>> handleIllegalAccessException(IllegalAccessException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ApiResponse<>(false, ex.getMessage(), null));
    }

    // IllegalArgumentException 예외 처리
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, ex.getMessage(), null));
    }

    // 일반적인 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, "내부 서버 오류가 발생했습니다.", null));
    }

}
