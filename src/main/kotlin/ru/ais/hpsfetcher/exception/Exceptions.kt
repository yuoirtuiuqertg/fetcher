//package ru.alfains.integration.sed.regcard.exception
//
//import ru.alfains.integration.sed.regcard.model.enums.ObjectType
//
//open class RegCardServiceBaseException(
//    override val message: String, cause: Throwable? = null
//) : RuntimeException(message, cause)
//
//class RegCardNotFoundException(id: Long) : RegCardServiceBaseException(
//    "Регистрационная карточка c id = $id не найдена"
//)
//
//class RegCardIdIsIncorrectException : RegCardServiceBaseException(
//    "Переданное значение для регистрационной карточки превышает максимально допустимое значение (9223372036854775807)"
//)
//
//class RegCardAlreadyExistsException(id: Long) : RegCardServiceBaseException(
//    "Регистрационная карточка c id = $id уже есть"
//)
//
//class ObjectTypeNotSpecifiedException : RegCardServiceBaseException(
//    "Не указан тип объекта"
//)
//
//class FileNotFoundException(fileId: Int) : RegCardServiceBaseException(
//    "Файл c id = $fileId не найден"
//)
//
//class ObjectNotFoundException(objectType: ObjectType, objectId: Long) : RegCardServiceBaseException(
//    "Объект с типом $objectType и id = $objectId не найден"
//)
//
//class ApplicantNotFoundException(applicantId: Long) : RegCardServiceBaseException(
//    "Заявитель с id = $applicantId не найден"
//)
//class FieldNotFoundException(
//    private val problemField: String,
//    message: String = "Поле $problemField не передано в запросе"
//) : RegCardServiceBaseException(message)
//
//class KeycloakRequestException(message: String, cause: Throwable? = null) :
//    RegCardServiceBaseException(message, cause)
//
//open class FileStorageClientException(
//    override val message: String, cause: Throwable? = null
//) : RegCardServiceBaseException(message, cause)
//
//class MappingException(message: String, cause: Throwable? = null) :
//    RegCardServiceBaseException(message, cause)
//
//class RetryRequestExceedException(message: String) :
//    RegCardServiceBaseException(message, null)
//
//class SpaceInputException : RegCardServiceBaseException(
//    "Не указано ни одного нового значения для обновления"
//)
//
//class UpdateInExternalSystemFailedException : RegCardServiceBaseException(
//    "Не удалось выполнить обновление во внешней системе"
//)
//
//class InvalidPropertyException(message: String) : RegCardServiceBaseException(message)