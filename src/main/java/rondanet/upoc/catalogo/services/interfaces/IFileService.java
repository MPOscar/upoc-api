package rondanet.upoc.catalogo.services.interfaces;

import rondanet.upoc.catalogo.exceptions.ServiceException;
import common.rondanet.clasico.core.catalogo.models.Image;

import java.io.IOException;
import java.util.List;

public interface IFileService {
	/**
	 * Tengo duda preguntarle a Luis, (Alexei)
	 * @param gln
	 * @param images
	 * @return
	 * @throws ServiceException
	 * @throws IOException
	 */
	public boolean SaveFileByGlnBussisnes(String gln, List<Image> images) throws ServiceException, IOException;

	void deleteFile(String location, String bucketName);

	boolean verificarQueExisteLaImagen(String location, String bucketName);
}

