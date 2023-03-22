API Pet Store

In this project, you will find Rest-Assured API and automated tests for the API - https://petstore.swagger.io/#/pet. The testing framework used in this project is TestNG.

Requirements
Before starting work on the project, make sure you have installed:

	Java Runtime Environment (JRE) version 17
	Maven version 3.8.1
	Jackson version 2.14.2
	JCommander version 1.82
	Rest-Assured version 5.3.0
	TestNG version 7.7.1

The links to download external libraries are placed in the pom.xml file.

Before executing the test, the user should enter their own file path to the photo in the pet.setPhotoUrls field in the addNewPet200 test in the apiPet class to ensure that the test runs correctly.

In the project, you can run all tests at once by setting the TestNG configuration Class parameter to "Store.apiStore, User.apiUser, Pet.apiPet".
Alternatively, you can run tests based on the class by selecting one of these fields. To execute individual tests, you need to select their name in code and then run as TestNG test.