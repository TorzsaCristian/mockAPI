import { SelectItem } from "primeng/api";

const FAKER_METHOD_OPTIONS: SelectItem[] = [
  { label: 'address.cityName', value: 'address.cityName' },
  { label: 'address.cityPrefix', value: 'address.cityPrefix' },
  { label: 'address.citySuffix', value: 'address.citySuffix' },
  { label: 'address.country', value: 'address.country' },
  { label: 'address.countryCode', value: 'address.countryCode' },
  { label: 'address.countyByZipCode', value: 'address.countyByZipCode' },
  { label: 'address.firstName', value: 'address.firstName' },
  { label: 'address.fullAddress', value: 'address.fullAddress' },
  { label: 'address.lastName', value: 'address.lastName' },
  { label: 'address.latitude', value: 'address.latitude' },
  { label: 'address.longitude', value: 'address.longitude' },
  { label: 'address.secondaryAddress', value: 'address.secondaryAddress' },
  { label: 'address.state', value: 'address.state' },
  { label: 'address.stateAbbr', value: 'address.stateAbbr' },
  { label: 'address.streetAddress', value: 'address.streetAddress' },
  { label: 'address.streetAddressNumber', value: 'address.streetAddressNumber' },
  { label: 'address.streetName', value: 'address.streetName' },
  { label: 'address.streetPrefix', value: 'address.streetPrefix' },
  { label: 'address.streetSuffix', value: 'address.streetSuffix' },
  { label: 'address.timeZone', value: 'address.timeZone' },
  { label: 'address.zipCode', value: 'address.zipCode' },
  { label: 'address.zipCodeByState', value: 'address.zipCodeByState' },

  { label: 'book.author', value: 'book.author' },
  { label: 'book.genre', value: 'book.genre' },
  { label: 'book.publisher', value: 'book.publisher' },
  { label: 'book.title', value: 'book.title' },

  { label: 'weather.description', value: 'weather.description' },
  { label: 'weather.temperatureCelsius', value: 'weather.temperatureCelsius' },
  { label: 'weather.temperatureCelsiusBetween', value: 'weather.temperatureCelsiusBetween' },
  { label: 'weather.temperatureFahrenheit', value: 'weather.temperatureFahrenheit' },
  { label: 'weather.temperatureFahrenheitBetween', value: 'weather.temperatureFahrenheitBetween' },

  { label: 'phone.cellPhone', value: 'phone.cellPhone' },
  { label: 'phone.phoneNumber', value: 'phone.phoneNumber' },

  { label: 'number.digit', value: 'number.digit' },
  { label: 'number.randomDigit', value: 'number.randomDigit' },
  { label: 'number.randomDigitNotZero', value: 'number.randomDigitNotZero' },
  { label: 'number.randomNumber', value: 'number.randomNumber' },

  { label: 'name.firstName', value: 'name.firstName' },
  { label: 'name.fullName', value: 'name.fullName' },
  { label: 'name.lastName', value: 'name.lastName' },
  { label: 'name.name', value: 'name.name' },
  { label: 'name.nameWithMiddle', value: 'name.nameWithMiddle' },
  { label: 'name.prefix', value: 'name.prefix' },
  { label: 'name.suffix', value: 'name.suffix' },
  { label: 'name.title', value: 'name.title' },
  { label: 'name.username', value: 'name.username' },

  { label: 'job.field', value: 'job.field' },
  { label: 'job.keySkills', value: 'job.keySkills' },
  { label: 'job.position', value: 'job.position' },
  { label: 'job.seniority', value: 'job.seniority' },
  { label: 'job.title', value: 'job.title' },

  { label: 'internet.avatar', value: 'internet.avatar' },
  { label: 'internet.domainName', value: 'internet.domainName' },
  { label: 'internet.domainSuffix', value: 'internet.domainSuffix' },
  { label: 'internet.domainWord', value: 'internet.domainWord' },
  { label: 'internet.emailAddress', value: 'internet.emailAddress' },
  { label: 'internet.image', value: 'internet.image' },
  { label: 'internet.ipV4Address', value: 'internet.ipV4Address' },
  { label: 'internet.ipV4Cidr', value: 'internet.ipV4Cidr' },
  { label: 'internet.ipV6Address', value: 'internet.ipV6Address' },
  { label: 'internet.ipV6Cidr', value: 'internet.ipV6Cidr' },
  { label: 'internet.macAddress', value: 'internet.macAddress' },
  { label: 'internet.password', value: 'internet.password' },
  { label: 'internet.privateIpV4Address', value: 'internet.privateIpV4Address' },
  { label: 'internet.publicIpV4Address', value: 'internet.publicIpV4Address' },
  { label: 'internet.safeEmailAddress', value: 'internet.safeEmailAddress' },
  { label: 'internet.slug', value: 'internet.slug' },
  { label: 'internet.url', value: 'internet.url' },
  { label: 'internet.userAgentAny', value: 'internet.userAgentAny' },
  { label: 'internet.uuid', value: 'internet.uuid' },

  { label: 'education.campus', value: 'education.campus' },
  { label: 'education.course', value: 'education.course' },
  { label: 'education.secondarySchool', value: 'education.secondarySchool' },
  { label: 'education.university', value: 'education.university' },


  { label: 'finance.bic', value: 'finance.bic' },
  { label: 'finance.creditCard', value: 'finance.creditCard' },
  { label: 'finance.iban', value: 'finance.iban' },

  { label: 'person.demonym', value: 'person.demonym' },
  { label: 'person.educationalAttainment', value: 'person.educationalAttainment' },
  { label: 'person.maritalStatus', value: 'person.maritalStatus' },
  { label: 'person.race', value: 'person.race' },
  { label: 'person.sex', value: 'person.sex' },

  { label: 'medical.diseaseName', value: 'medical.diseaseName' },
  { label: 'medical.hospitalName', value: 'medical.hospitalName' },
  { label: 'medical.medicineName', value: 'medical.medicineName' },
  { label: 'medical.symptoms', value: 'medical.symptoms' },


];


export default FAKER_METHOD_OPTIONS;
