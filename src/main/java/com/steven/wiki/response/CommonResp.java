package com.steven.wiki.response;

//统一后端返回值格式，set up a common response format
public class CommonResp<T> {

    /**
     * indicator for task success or fail, 业务成功或失败
     */
    private boolean success = true;

    /**
     * Return value, 返回的信息
     */
    private String message;

    /**
     * Return generic value, customized type, 返回自定义类型泛型数据
     */
    private T content;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ResponseDto{");
        sb.append("success= ").append(success);
        sb.append(", message=' ").append(message).append('\'');
        sb.append(", content= ").append(content);
        sb.append('}');
        return sb.toString();
    }
}
