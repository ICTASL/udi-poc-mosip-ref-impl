package io.mosip.kernel.masterdata.test.controller;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import io.mosip.kernel.masterdata.dto.ApplicationDto;
import io.mosip.kernel.masterdata.dto.BiometricAttributeDto;
import io.mosip.kernel.masterdata.dto.BiometricTypeDto;
import io.mosip.kernel.masterdata.dto.BiometricTypeResponseDto;
import io.mosip.kernel.masterdata.dto.DocumentCategoryDto;
import io.mosip.kernel.masterdata.dto.DocumentTypeDto;
import io.mosip.kernel.masterdata.dto.LanguageDto;
import io.mosip.kernel.masterdata.dto.LanguageRequestResponseDto;
import io.mosip.kernel.masterdata.dto.LocationDto;
import io.mosip.kernel.masterdata.dto.LocationResponseDto;
import io.mosip.kernel.masterdata.dto.TemplateDto;
import io.mosip.kernel.masterdata.dto.ValidDocumentTypeResponseDto;
import io.mosip.kernel.masterdata.entity.Holiday;
import io.mosip.kernel.masterdata.entity.HolidayId;
import io.mosip.kernel.masterdata.entity.IdType;
import io.mosip.kernel.masterdata.entity.RegistrationCenter;
import io.mosip.kernel.masterdata.exception.DataNotFoundException;
import io.mosip.kernel.masterdata.exception.MasterDataServiceException;
import io.mosip.kernel.masterdata.repository.HolidayRepository;
import io.mosip.kernel.masterdata.repository.IdTypeRepository;
import io.mosip.kernel.masterdata.repository.RegistrationCenterRepository;
import io.mosip.kernel.masterdata.service.ApplicationService;
import io.mosip.kernel.masterdata.service.BiometricAttributeService;
import io.mosip.kernel.masterdata.service.BiometricTypeService;
import io.mosip.kernel.masterdata.service.DocumentCategoryService;
import io.mosip.kernel.masterdata.service.DocumentTypeService;
import io.mosip.kernel.masterdata.service.LanguageService;
import io.mosip.kernel.masterdata.service.LocationService;
import io.mosip.kernel.masterdata.service.TemplateService;

/**
 * 
 * @author Bal Vikash Sharma
 * @since 1.0.0
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MasterdataControllerTest {

	@Autowired
	public MockMvc mockMvc;

	@MockBean
	private BiometricTypeService biometricTypeService;

	private static final String BIOMETRICTYPE_EXPECTED_LIST = "[\n" + "  {\n" + "    \"code\": \"1\",\n"
			+ "    \"name\": \"DNA MATCHING\",\n" + "    \"description\": null,\n" + "    \"langCode\": \"ENG\"\n"
			+ "  },\n" + "  {\n" + "    \"code\": \"3\",\n" + "    \"name\": \"EYE SCAN\",\n"
			+ "    \"description\": null,\n" + "    \"langCode\": \"ENG\"\n" + "  }\n" + "]";

	private static final String BIOMETRICTYPE_EXPECTED_OBJECT = "{\n" + "    \"code\": \"1\",\n"
			+ "    \"name\": \"DNA MATCHING\",\n" + "    \"description\": null,\n" + "    \"langCode\": \"ENG\"\n"
			+ "  }";

	private BiometricTypeDto biometricTypeDto1 = new BiometricTypeDto();
	private BiometricTypeDto biometricTypeDto2 = new BiometricTypeDto();

	private List<BiometricTypeDto> biometricTypeDtoList = new ArrayList<>();

	@MockBean
	private ApplicationService applicationService;

	private static final String APPLIACTION_EXPECTED_LIST = "[\n" + "  {\n" + "    \"code\": \"101\",\n"
			+ "    \"name\": \"pre-registeration\",\n" + "    \"description\": \"Pre-registration Application Form\",\n"
			+ "    \"languageCode\": \"ENG\"\n" + "  }\n" + "]";

	private static final String APPLIACTION_EXPECTED_OBJECT = "{\n" + "    \"code\": \"101\",\n"
			+ "    \"name\": \"pre-registeration\",\n" + "    \"description\": \"Pre-registration Application Form\",\n"
			+ "    \"languageCode\": \"ENG\"\n" + "  }";

	private ApplicationDto applicationDto = new ApplicationDto();

	private List<ApplicationDto> applicationDtoList = new ArrayList<>();

	@MockBean
	private BiometricAttributeService biometricAttributeService;

	private final String BIOMETRIC_ATTRIBUTE_EXPECTED = "{ \"biometricattributes\": [ { \"code\": \"iric_black\", \"name\": \"black\", \"description\": null, \"isActive\": true},{\"code\": \"iric_brown\", \"name\": \"brown\", \"description\": null,\"isActive\": true } ] }";

	BiometricTypeResponseDto biometricTypeResponseDto = null;
	List<BiometricAttributeDto> biometricattributes = null;

	private static final String DOCUMENT_CATEGORY_EXPECTED_LIST = "[\n" + "  {\n" + "    \"code\": \"101\",\n"
			+ "    \"name\": \"POI\",\n" + "    \"description\": null,\n" + "    \"langCode\": \"ENG\"\n" + "  },\n"
			+ "  {\n" + "    \"code\": \"102\",\n" + "    \"name\": \"POR\",\n" + "    \"description\": null,\n"
			+ "    \"langCode\": \"ENG\"\n" + "  }\n" + "]";

	private static final String DOCUMENT_CATEGORY_EXPECTED_OBJECT = "{\n" + "    \"code\": \"101\",\n"
			+ "    \"name\": \"POI\",\n" + "    \"description\": null,\n" + "    \"langCode\": \"ENG\"\n" + "  }";

	private DocumentCategoryDto documentCategoryDto1;
	private DocumentCategoryDto documentCategoryDto2;

	private List<DocumentCategoryDto> documentCategoryDtoList = new ArrayList<>();

	@MockBean
	private DocumentCategoryService documentCategoryService;

	@MockBean
	private DocumentTypeService documentTypeService;

	private final String DOCUMENT_TYPE_EXPECTED = "{ \"documents\": [ { \"code\": \"addhar\", \"name\": \"adhar card\", \"description\": \"Uid\", \"langCode\": \"eng\"}, { \"code\": \"residensial\", \"name\": \"residensial_prof\", \"description\": \"document for residential prof\", \"langCode\": \"eng\" } ] }";

	ValidDocumentTypeResponseDto validDocumentTypeResponseDto = null;

	List<DocumentTypeDto> documentTypeDtos = null;

	@MockBean
	private IdTypeRepository repository;

	private IdType idType;

	@MockBean
	private LanguageService languageService;

	private static final String LANGUAGE_JSON_STRING = "{ \"languages\": [   {      \"languageCode\": \"hin\", \"languageName\": \"hindi\",      \"languageFamily\": \"hindi\",   \"nativeName\": \"hindi\" } ]}";

	private LanguageRequestResponseDto respDto;
	private List<LanguageDto> languages;
	private LanguageDto hin;

	private final String LOCATION_JSON_EXPECTED = "{\"locations\":[{\"locationCode\":\"KAR\",\"locationName\":\"KARNATAKA\",\"hierarchyLevel\":1,\"hierarchyName\":null,\"parentLocationCode\":\"IND\",\"languageCode\":\"KAN\",\"createdBy\":\"dfs\",\"updatedBy\":\"sdfsd\",\"isActive\":true},{\"locationCode\":\"KAR\",\"locationName\":\"KARNATAKA\",\"hierarchyLevel\":1,\"hierarchyName\":null,\"parentLocationCode\":\"IND\",\"languageCode\":\"KAN\",\"createdBy\":\"dfs\",\"updatedBy\":\"sdfsd\",\"isActive\":true}]}";

	@MockBean
	private LocationService locationService;

	LocationDto locationDto = null;
	LocationResponseDto locationResponseDto = null;

	@MockBean
	private HolidayRepository holidayRepository;
	@MockBean
	private RegistrationCenterRepository registrationCenterRepository;

	private RegistrationCenter registrationCenter;
	private List<Holiday> holidays;

	@MockBean
	private TemplateService templateService;

	private static final String TEMPLATE_EXPECTED_LIST = "[\r\n" + "  {\r\n" + "	\"id\": \"3\",\r\n"
			+ "    \"name\": \"Email template\",\r\n" + "    \"description\": null,\r\n"
			+ "	\"fileFormatCode\": \"xml\",\r\n" + "	\"model\": null,\r\n" + "	\"fileText\": null,\r\n"
			+ "	\"moduleId\": null,\r\n" + "	\"moduleName\": null,\r\n" + "	\"templateTypeCode\": \"EMAIL\",\r\n"
			+ "    \"languageCode\": \"HIN\"\r\n" + "  }\r\n" + "]";

	private List<TemplateDto> templateDtoList = new ArrayList<>();

	@Before
	public void setUp() {
		biometricTypeSetup();

		applicationSetup();

		biometricAttributeSetup();

		// TODO DeviceControllerTest
		// TODO DeviceSpecificationControllerTest

		documentCategorySetup();

		documentTypeSetup();

		idTypeSetup();

		locationSetup();
		// TODO MachineDetailControllerTest
		// TODO MachineHistoryControllerTest

		registrationCenterController();

		templateSetup();

	}

	private void templateSetup() {
		TemplateDto templateDto = new TemplateDto();

		templateDto.setId("3");
		templateDto.setName("Email template");
		templateDto.setFileFormatCode("xml");
		templateDto.setTemplateTypeCode("EMAIL");
		templateDto.setLanguageCode("HIN");

		templateDtoList.add(templateDto);
	}

	private void registrationCenterController() {
		LocalDateTime specificDate = LocalDateTime.of(2018, Month.JANUARY, 1, 10, 10, 30);
		LocalDate date = LocalDate.of(2018, Month.NOVEMBER, 7);
		registrationCenter = new RegistrationCenter();
		registrationCenter.setAddressLine1("7th Street");
		registrationCenter.setAddressLine2("Lane 2");
		registrationCenter.setAddressLine3("Mylasandra-560001");
		registrationCenter.setIsActive(true);
		registrationCenter.setCenterTypeCode("PAR");
		registrationCenter.setContactPhone("987654321");
		registrationCenter.setCreatedBy("John");
		registrationCenter.setCreatedtimes(specificDate);
		registrationCenter.setHolidayLocationCode("KAR");
		registrationCenter.setLocationCode("KAR_59");
		registrationCenter.setId("REG_CR_001");
		registrationCenter.setLanguageCode("ENG");
		registrationCenter.setWorkingHours("9");
		registrationCenter.setLatitude("12.87376");
		registrationCenter.setLongitude("12.76372");
		registrationCenter.setName("RV Niketan REG CENTER");

		holidays = new ArrayList<>();

		Holiday holiday = new Holiday();
		holiday.setHolidayId(new HolidayId(1, "KAR", date, "ENG"));
		holiday.setHolidayName("Diwali");
		holiday.setCreatedBy("John");
		holiday.setCreatedtimes(specificDate);
		holiday.setHolidayDesc("Diwali");
		holiday.setIsActive(true);

		holidays.add(holiday);
	}

	private void locationSetup() {
		List<LocationDto> locationHierarchies = new ArrayList<>();
		locationResponseDto = new LocationResponseDto();
		locationDto = new LocationDto();
		locationDto.setLocationCode("IND");
		locationDto.setLocationName("INDIA");
		locationDto.setHierarchyLevel(0);
		locationDto.setHierarchyName(null);
		locationDto.setParentLocationCode(null);
		locationDto.setLanguageCode("HIN");
		locationDto.setCreatedBy("dfs");
		locationDto.setUpdatedBy("sdfsd");
		locationDto.setIsActive(true);
		locationHierarchies.add(locationDto);
		locationDto.setLocationCode("KAR");
		locationDto.setLocationName("KARNATAKA");
		locationDto.setHierarchyLevel(1);
		locationDto.setHierarchyName(null);
		locationDto.setParentLocationCode("IND");
		locationDto.setLanguageCode("KAN");
		locationDto.setCreatedBy("dfs");
		locationDto.setUpdatedBy("sdfsd");
		locationDto.setIsActive(true);
		locationHierarchies.add(locationDto);
		locationResponseDto.setLocations(locationHierarchies);
	}

	private void idTypeSetup() {
		idType = new IdType();
		idType.setActive(true);
		idType.setCrBy("testCreation");
		idType.setLangCode("ENG");
		idType.setCode("POA");
		idType.setDescr("Proof Of Address");
	}

	private void documentTypeSetup() {
		documentTypeDtos = new ArrayList<DocumentTypeDto>();
		DocumentTypeDto documentType = new DocumentTypeDto();
		documentType.setCode("addhar");
		documentType.setName("adhar card");
		documentType.setDescription("Uid");
		documentType.setLangCode("eng");
		// documentType.setIsActive(true);
		documentTypeDtos.add(documentType);
		DocumentTypeDto documentType1 = new DocumentTypeDto();
		documentType1.setCode("residensial");
		documentType1.setName("residensial_prof");
		documentType1.setDescription("document for residential prof");
		documentType1.setLangCode("eng");
		// documentType1.setIsActive(true);
		documentTypeDtos.add(documentType1);
		validDocumentTypeResponseDto = new ValidDocumentTypeResponseDto(documentTypeDtos);
	}

	private void documentCategorySetup() {
		documentCategoryDto1 = new DocumentCategoryDto();
		documentCategoryDto1.setCode("101");
		documentCategoryDto1.setName("POI");
		documentCategoryDto1.setLangCode("ENG");

		documentCategoryDto2 = new DocumentCategoryDto();
		documentCategoryDto2.setCode("102");
		documentCategoryDto2.setName("POR");
		documentCategoryDto2.setLangCode("ENG");

		documentCategoryDtoList.add(documentCategoryDto1);
		documentCategoryDtoList.add(documentCategoryDto2);
	}

	private void biometricAttributeSetup() {
		biometricattributes = new ArrayList<>();
		BiometricAttributeDto biometricAttribute = new BiometricAttributeDto();
		biometricAttribute.setCode("iric_black");
		biometricAttribute.setName("black");
		biometricAttribute.setDescription(null);
		biometricAttribute.setIsActive(true);
		biometricattributes.add(biometricAttribute);
		BiometricAttributeDto biometricAttribute1 = new BiometricAttributeDto();
		biometricAttribute1.setCode("iric_brown");
		biometricAttribute1.setName("brown");
		biometricAttribute.setDescription(null);
		biometricAttribute1.setIsActive(true);
		biometricattributes.add(biometricAttribute1);
		biometricTypeResponseDto = new BiometricTypeResponseDto(biometricattributes);
	}

	private void applicationSetup() {
		applicationDto.setCode("101");
		applicationDto.setName("pre-registeration");
		applicationDto.setDescription("Pre-registration Application Form");
		applicationDto.setLanguageCode("ENG");

		applicationDtoList.add(applicationDto);
	}

	private void biometricTypeSetup() {
		biometricTypeDto1.setCode("1");
		biometricTypeDto1.setName("DNA MATCHING");
		biometricTypeDto1.setDescription(null);
		biometricTypeDto1.setLangCode("ENG");

		biometricTypeDto2.setCode("3");
		biometricTypeDto2.setName("EYE SCAN");
		biometricTypeDto2.setDescription(null);
		biometricTypeDto2.setLangCode("ENG");

		biometricTypeDtoList.add(biometricTypeDto1);
		biometricTypeDtoList.add(biometricTypeDto2);
	}

	@Test
	public void fetchAllBioMetricTypeTest() throws Exception {

		Mockito.when(biometricTypeService.getAllBiometricTypes()).thenReturn(biometricTypeDtoList);

		mockMvc.perform(MockMvcRequestBuilders.get("/biometrictypes"))
				.andExpect(MockMvcResultMatchers.content().json(BIOMETRICTYPE_EXPECTED_LIST))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void fetchAllBiometricTypeUsingLangCodeTest() throws Exception {
		Mockito.when(biometricTypeService.getAllBiometricTypesByLanguageCode(Mockito.anyString()))
				.thenReturn(biometricTypeDtoList);
		mockMvc.perform(MockMvcRequestBuilders.get("/biometrictypes/ENG"))
				.andExpect(MockMvcResultMatchers.content().json(BIOMETRICTYPE_EXPECTED_LIST))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void fetchBiometricTypeUsingCodeAndLangCode() throws Exception {
		Mockito.when(biometricTypeService.getBiometricTypeByCodeAndLangCode(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(biometricTypeDto1);
		mockMvc.perform(MockMvcRequestBuilders.get("/biometrictypes/1/ENG"))
				.andExpect(MockMvcResultMatchers.content().json(BIOMETRICTYPE_EXPECTED_OBJECT))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	// -------------------------------ApplicationControllerTest--------------------------

	@Test
	public void fetchAllApplicationTest() throws Exception {

		Mockito.when(applicationService.getAllApplication()).thenReturn(applicationDtoList);

		mockMvc.perform(MockMvcRequestBuilders.get("/applicationtypes"))
				.andExpect(MockMvcResultMatchers.content().json(APPLIACTION_EXPECTED_LIST))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void fetchAllApplicationUsingLangCodeTest() throws Exception {
		Mockito.when(applicationService.getAllApplicationByLanguageCode(Mockito.anyString()))
				.thenReturn(applicationDtoList);
		mockMvc.perform(MockMvcRequestBuilders.get("/applicationtypes/ENG"))
				.andExpect(MockMvcResultMatchers.content().json(APPLIACTION_EXPECTED_LIST))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void fetchApplicationUsingCodeAndLangCode() throws Exception {
		Mockito.when(applicationService.getApplicationByCodeAndLanguageCode(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(applicationDto);
		mockMvc.perform(MockMvcRequestBuilders.get("/applicationtypes/101/ENG"))
				.andExpect(MockMvcResultMatchers.content().json(APPLIACTION_EXPECTED_OBJECT))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	// -------------------------------BiometricAttributeControllerTest--------------------------

	@Test
	public void testGetBiometricAttributesByBiometricType() throws Exception {

		Mockito.when(biometricAttributeService.getBiometricAttribute(Mockito.anyString(), Mockito.anyString()))
				.thenReturn((biometricattributes));
		mockMvc.perform(MockMvcRequestBuilders.get("/getbiometricattributesbyauthtype/eng/iric"))
				.andExpect(MockMvcResultMatchers.content().json(BIOMETRIC_ATTRIBUTE_EXPECTED))
				.andExpect(MockMvcResultMatchers.status().isOk());

	}

	@Test
	public void testBiometricTypeBiometricAttributeNotFoundException() throws Exception {
		Mockito.when(biometricAttributeService.getBiometricAttribute(Mockito.anyString(), Mockito.anyString()))
				.thenThrow(new DataNotFoundException("KER-MAS-00000",
						"No biometric attributes found for specified biometric code type and language code"));
		mockMvc.perform(MockMvcRequestBuilders.get("/getbiometricattributesbyauthtype/eng/face"))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	public void testBiometricTypeFetchException() throws Exception {
		Mockito.when(biometricAttributeService.getBiometricAttribute(Mockito.anyString(), Mockito.anyString()))
				.thenThrow(new MasterDataServiceException("KER-DOC-00000", "exception duringfatching data from db"));
		mockMvc.perform(MockMvcRequestBuilders.get("/getbiometricattributesbyauthtype/eng/iric"))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());
	}

	// -------------------------------DocumentCategoryControllerTest--------------------------

	// @Test
	// public void fetchAllDocumentCategoryTest() throws Exception {
	//
	// Mockito.when(documentCategoryService.getAllDocumentCategory()).thenReturn(documentCategoryDtoList);
	//
	// mockMvc.perform(MockMvcRequestBuilders.get("/documentcategories"))
	// .andExpect(MockMvcResultMatchers.content().json(DOCUMENT_CATEGORY_EXPECTED_LIST))
	// .andExpect(MockMvcResultMatchers.status().isOk());
	// }
	//
	// @Test
	// public void fetchAllDocumentCategoryUsingLangCodeTest() throws Exception {
	//
	// Mockito.when(documentCategoryService.getAllDocumentCategoryByLaguageCode(Mockito.anyString()))
	// .thenReturn(documentCategoryDtoList);
	//
	// mockMvc.perform(MockMvcRequestBuilders.get("/documentcategories/ENG"))
	// .andExpect(MockMvcResultMatchers.content().json(DOCUMENT_CATEGORY_EXPECTED_LIST))
	// .andExpect(MockMvcResultMatchers.status().isOk());
	// }
	//
	// @Test
	// public void fetchDocumentCategoryUsingCodeAndLangCodeTest() throws Exception
	// {
	//
	// Mockito.when(documentCategoryService.getDocumentCategoryByCodeAndLangCode(Mockito.anyString(),
	// Mockito.anyString()))
	// .thenReturn(documentCategoryDto1);
	//
	// mockMvc.perform(MockMvcRequestBuilders.get("/documentcategories/101/ENG"))
	// .andExpect(MockMvcResultMatchers.content().json(DOCUMENT_CATEGORY_EXPECTED_OBJECT))
	// .andExpect(MockMvcResultMatchers.status().isOk());
	// }

	// -------------------------------DocumentTypeControllerTest--------------------------
	@Test
	public void testGetDoucmentTypesForDocumentCategoryAndLangCode() throws Exception {

		Mockito.when(documentTypeService.getAllValidDocumentType(Mockito.anyString(), Mockito.anyString()))
				.thenReturn((documentTypeDtos));
		mockMvc.perform(MockMvcRequestBuilders.get("/documenttypes/poa/eng"))
				.andExpect(MockMvcResultMatchers.content().json(DOCUMENT_TYPE_EXPECTED))
				.andExpect(MockMvcResultMatchers.status().isOk());

	}

	@Test
	public void testDocumentCategoryNotFoundException() throws Exception {
		Mockito.when(documentTypeService.getAllValidDocumentType(Mockito.anyString(), Mockito.anyString()))
				.thenThrow(new DataNotFoundException("KER-DOC-10001",
						"No documents found for specified document category code and language code"));
		mockMvc.perform(MockMvcRequestBuilders.get("/documenttypes/poc/eng"))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	public void testDocumentCategoryFetchException() throws Exception {
		Mockito.when(documentTypeService.getAllValidDocumentType(Mockito.anyString(), Mockito.anyString()))
				.thenThrow(new MasterDataServiceException("KER-DOC-10000", "exception during fatching data from db"));
		mockMvc.perform(MockMvcRequestBuilders.get("/documenttypes/poc/eng"))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());
	}
	// -------------------------------IdTypesControllerTest--------------------------

	@Test
	public void testIdTypeController() throws Exception {
		List<IdType> idTypeList = new ArrayList<>();
		idTypeList.add(idType);
		Mockito.when(repository.findByLangCodeAndIsActiveTrueAndIsDeletedFalse(anyString())).thenReturn(idTypeList);
		mockMvc.perform(get("/idtypes/{languagecode}", "ENG")).andExpect(status().isOk());
	}

	// -------------------------------LanguageControllerTest--------------------------
	@Test
	public void testGetAllLanguages() throws Exception {
		loadSuccessData();
		Mockito.when(languageService.getAllLaguages()).thenReturn(respDto);

		mockMvc.perform(MockMvcRequestBuilders.get("/languages"))
				.andExpect(MockMvcResultMatchers.content().json(LANGUAGE_JSON_STRING))
				.andExpect(MockMvcResultMatchers.status().isOk());

	}

	@Test
	public void testGetAllLanguagesForLanguageNotFoundException() throws Exception {
		Mockito.when(languageService.getAllLaguages())
				.thenThrow(new DataNotFoundException("KER-MAS-0987", "No Language found"));
		mockMvc.perform(MockMvcRequestBuilders.get("/languages"))
				.andExpect(MockMvcResultMatchers.status().isNotFound());

	}

	@Test
	public void testGetAllLanguagesForLanguageFetchException() throws Exception {
		Mockito.when(languageService.getAllLaguages())
				.thenThrow(new MasterDataServiceException("KER-MAS-0988", "Error occured while fetching language"));
		mockMvc.perform(MockMvcRequestBuilders.get("/languages"))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());
	}

	private void loadSuccessData() {
		respDto = new LanguageRequestResponseDto();
		languages = new ArrayList<>();

		// creating language
		hin = new LanguageDto();
		hin.setLanguageCode("hin");
		hin.setLanguageName("hindi");
		hin.setLanguageFamily("hindi");
		hin.setNativeName("hindi");

		// adding language to list
		languages.add(hin);

		respDto.setLanguages(languages);

	}

	// -------------------------------LocationControllerTest--------------------------

	@Test
	public void testGetAllLocationHierarchy() throws Exception {

		Mockito.when(locationService.getLocationDetails()).thenReturn(locationResponseDto);
		mockMvc.perform(MockMvcRequestBuilders.get("/locations"))
				.andExpect(MockMvcResultMatchers.content().json(LOCATION_JSON_EXPECTED))
				.andExpect(MockMvcResultMatchers.status().isOk());

	}

	@Test
	public void testGetLocatonHierarchyByLocCodeAndLangCode() throws Exception {
		Mockito.doReturn(locationResponseDto).when(locationService).getLocationHierarchyByLangCode(Mockito.anyString(),
				Mockito.anyString());

		mockMvc.perform(MockMvcRequestBuilders.get("/locations/KAR/KAN"))
				.andExpect(MockMvcResultMatchers.content().json(LOCATION_JSON_EXPECTED))
				.andExpect(MockMvcResultMatchers.status().isOk());

	}

	@Test
	public void testGetAllLocationsNoRecordsFoundException() throws Exception {
		Mockito.when(locationService.getLocationDetails())
				.thenThrow(new MasterDataServiceException("1111111", "Error from database"));
		mockMvc.perform(MockMvcRequestBuilders.get("/locations"))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());
	}

	@Test
	public void testGetAllLocationsDataBaseException() throws Exception {
		Mockito.when(locationService.getLocationDetails())
				.thenThrow(new DataNotFoundException("3333333", "Location Hierarchy does not exist"));
		mockMvc.perform(MockMvcRequestBuilders.get("/locations"))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	public void testGetLocationsByLangCodeAndLocCodeDataBaseException() throws Exception {
		Mockito.when(locationService.getLocationHierarchyByLangCode(Mockito.anyString(), Mockito.anyString()))
				.thenThrow(new MasterDataServiceException("1111111", "Error from database"));
		mockMvc.perform(MockMvcRequestBuilders.get("/locations/KAR/KAN"))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());
	}

	@Test
	public void testGetLocationsByLangCodeAndLocCodeNoRecordsFoundException() throws Exception {
		Mockito.when(locationService.getLocationHierarchyByLangCode(Mockito.anyString(), Mockito.anyString()))
				.thenThrow(new DataNotFoundException("3333333", "Location Hierarchy does not exist"));
		mockMvc.perform(MockMvcRequestBuilders.get("/locations/KAR/KAN"))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	// -------------------------------RegistrationCenterControllerTest--------------------------

	@Test
	public void testGetRegistraionCenterHolidaysSuccess() throws Exception {
		Mockito.when(registrationCenterRepository.findByIdAndLanguageCodeAndIsActiveTrueAndIsDeletedFalse(anyString(),
				anyString())).thenReturn(registrationCenter);
		Mockito.when(holidayRepository.findAllByLocationCodeYearAndLangCode(anyString(), anyString(), anyInt()))
				.thenReturn(holidays);
		mockMvc.perform(get("/getregistrationcenterholidays/{languagecode}/{registrationcenterid}/{year}", "ENG",
				"REG_CR_001", 2018)).andExpect(status().isOk());
	}

	@Test
	public void testGetRegistraionCenterHolidaysNoRegCenterFound() throws Exception {
		mockMvc.perform(get("/getregistrationcenterholidays/{languagecode}/{registrationcenterid}/{year}", "ENG",
				"REG_CR_001", 2017)).andExpect(status().isNotFound());
	}

	@Test
	public void testGetRegistraionCenterHolidaysRegistrationCenterFetchException() throws Exception {
		Mockito.when(registrationCenterRepository.findByIdAndLanguageCodeAndIsActiveTrueAndIsDeletedFalse(anyString(),
				anyString())).thenThrow(DataRetrievalFailureException.class);
		mockMvc.perform(get("/getregistrationcenterholidays/{languagecode}/{registrationcenterid}/{year}", "ENG",
				"REG_CR_001", 2017)).andExpect(status().isInternalServerError());
	}

	@Test
	public void testGetRegistraionCenterHolidaysHolidayFetchException() throws Exception {
		Mockito.when(registrationCenterRepository.findByIdAndLanguageCodeAndIsActiveTrueAndIsDeletedFalse(anyString(),
				anyString())).thenReturn(registrationCenter);
		Mockito.when(holidayRepository.findAllByLocationCodeYearAndLangCode(anyString(), anyString(), anyInt()))
				.thenThrow(DataRetrievalFailureException.class);
		mockMvc.perform(get("/getregistrationcenterholidays/{languagecode}/{registrationcenterid}/{year}", "ENG",
				"REG_CR_001", 2018)).andExpect(status().isInternalServerError());
	}

	// -------------------------------TemplateControllerTest--------------------------

	@Test
	public void getAllTemplateByTest() throws Exception {

		Mockito.when(templateService.getAllTemplate()).thenReturn(templateDtoList);

		mockMvc.perform(MockMvcRequestBuilders.get("/templates"))
				.andExpect(MockMvcResultMatchers.content().json(TEMPLATE_EXPECTED_LIST)).andExpect(status().isOk());

	}

	@Test
	public void getAllTemplateByLanguageCodeTest() throws Exception {

		Mockito.when(templateService.getAllTemplateByLanguageCode(Mockito.anyString())).thenReturn(templateDtoList);

		mockMvc.perform(MockMvcRequestBuilders.get("/templates/HIN"))
				.andExpect(MockMvcResultMatchers.content().json(TEMPLATE_EXPECTED_LIST)).andExpect(status().isOk());
	}

	@Test
	public void getAllTemplateByLanguageCodeAndTemplateTypeCodeTest() throws Exception {

		Mockito.when(templateService.getAllTemplateByLanguageCodeAndTemplateTypeCode(Mockito.anyString(),
				Mockito.anyString())).thenReturn(templateDtoList);

		mockMvc.perform(MockMvcRequestBuilders.get("/templates/HIN/EMAIL"))
				.andExpect(MockMvcResultMatchers.content().json(TEMPLATE_EXPECTED_LIST)).andExpect(status().isOk());
	}
}
