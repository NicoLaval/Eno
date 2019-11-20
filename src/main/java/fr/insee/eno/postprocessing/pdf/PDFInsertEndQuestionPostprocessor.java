package fr.insee.eno.postprocessing.pdf;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.insee.eno.Constants;
import fr.insee.eno.parameters.PostProcessing;
import fr.insee.eno.postprocessing.Postprocessor;
import fr.insee.eno.transform.xsl.XslTransformation;

/**
 * A PDF post processing
 */
public class PDFInsertEndQuestionPostprocessor implements Postprocessor {

	private static final Logger logger = LoggerFactory.getLogger(PDFInsertEndQuestionPostprocessor.class);

	private XslTransformation saxonService = new XslTransformation();

	@Override
	public File process(File input, byte[] parameters, String survey) throws Exception {

		File outputForFOFile = new File(input.getParent(),
				Constants.BASE_NAME_FORM_FILE +
				Constants.END_QUESTION_FO_EXTENSION);
		logger.debug("Output folder for basic-form : " + outputForFOFile.getAbsolutePath());
		
		String surveyName = survey;
		String formName = getFormName(input);

		InputStream FO_XSL = Constants.getInputStreamFromPath(Constants.TRANSFORMATIONS_END_QUESTION_FO_4PDF);

		InputStream inputStream = FileUtils.openInputStream(input);
		OutputStream outputStream = FileUtils.openOutputStream(outputForFOFile);

		saxonService.transformFOToStep4FO(inputStream, outputStream, FO_XSL, surveyName, formName, parameters);

		inputStream.close();
		outputStream.close();
		FO_XSL.close();
		logger.info("End of InsertEndQuestion post-processing " + outputForFOFile.getAbsolutePath());

		return outputForFOFile;
	}

	private String getFormName(File input) {
		return FilenameUtils.getBaseName(input.getParentFile().getParent());
	}
	
	public String toString() {
		return PostProcessing.PDF_INSERT_END_QUESTION.name();
	}

}
