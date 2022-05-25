package rondanet.upoc.core.utils;

public interface IEmailHelper {

	public void SendEmail(String email, String mensaje);

	public void SendEmailError(String email, String mensaje, Exception error);

	public void sendErrorEmail(String collecion, Exception error);

}
