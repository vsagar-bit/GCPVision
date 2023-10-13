
>> We have used Android application for product image capturing, Google cloud vision to get lable for image and Alpha Vantage API to fetch stock information form image lable. 

	The application workflow is as below.
	
		1. Android application will capture image of product using intent, the image would be saved to google storage and get the image file location.
		2. With the image path from google storage mobile app calls the hosted API/Google Cloud Functions to fetch the stocks information for the image product company.
		3. Our hosted API/Google Cloud Function calls cloud vision SDK client with the image bite data from google storage to get the lables for the image product and and scores of lable probability.
		4. With the highest score lable we call Alpha Vantage API to fetch stock information of image lable company.
	
	We can improve to add authenticate the user of the Android application, eliminate PII/adult imagery search by using SafeSearch in cloud vision.
	
			SafeSearchAnnotation safeSearch = responses.getResponses(0).getSafeSearchAnnotation();
					System.out.println("SafeSearch Annotation:");
					System.out.println("Adult: " + safeSearch.getAdult());
					System.out.println("Spoof: " + safeSearch.getSpoof());
					System.out.println("Medical: " + safeSearch.getMedical());
					System.out.println("Violence: " + safeSearch.getViolence());


>> Main dependencies :
		<dependency>
			<groupId>com.google.cloud</groupId>
			<artifactId>google-cloud-vision</artifactId>
		</dependency>
		<dependency>
			<groupId>org.apache.httpcomponents</groupId>
			<artifactId>httpclient</artifactId>
		</dependency>

>> To generate a GCP service account and obtain the credentials:

      Go to the Google Cloud Console: https://console.cloud.google.com/
      Navigate to the "IAM & Admin" > "Service accounts" section.
      Click on "Create Service Account".
      Enter a name and optional description for your service account, then click "Create".
      On the "Role" step, add the appropriate roles (e.g., "Project" > "Editor" for full access). Click "Continue" and then "Done".
      Find the newly created service account in the list and click on the "Edit" icon (pencil).
      Click "Add Key" and choose "JSON" to download the credentials.json file.
	  
	  **sample credentials json file :
	  
			{
			  "type": "service_account",
			  "project_id": "your-project-id",
			  "private_key_id": "your-private-key-id",
			  "private_key": "-----BEGIN PRIVATE KEY-----\nYOUR-PRIVATE-KEY\n-----END PRIVATE KEY-----",
			  "client_email": "your-service-account-email",
			  "client_id": "your-client-id",
			  "auth_uri": "https://accounts.google.com/o/oauth2/auth",
			  "token_uri": "https://accounts.google.com/o/oauth2/token",
			  "auth_provider_x509_cert_url": "https://www.googleapis.com/oauth2/v1/certs",
			  "client_x509_cert_url": "https://www.googleapis.com/robot/v1/metadata/x509/your-service-account-email"
			}

>> To activate service account insall google cloud sdk shell(CLI) 
      run the command "gcloud auth activate-service-account --key-file=<your-creds.json path>"

	  
>> For fetching stocks information by company name and stock name, we are using the Alpha Vantage API:

      Sign Up and Obtain API Key:
      Go to the Alpha Vantage website (https://www.alphavantage.co/) and sign up for a free or premium account.
      Obtain an API key, which you'll use to authenticate your requests to their API.

>> If you're looking for alternatives to Alpha Vantage for fetching stock-related data and options data, here are a few popular options:

      Yahoo Finance API:
      
      Yahoo Finance offers a widely used API to fetch a variety of financial data, including stock quotes, historical data, and options data.
      API Documentation: https://rapidapi.com/apidojo/api/yahoo-finance1

      IEX Cloud API:
      
      IEX Cloud provides a comprehensive financial data platform, including stock and options data. They offer a simple and well-documented API.
      API Documentation: https://iexcloud.io/docs/api/

      Polygon.io:
      
      Polygon.io offers real-time and historic market data, including stock and options data. They provide a developer-friendly API with various pricing options.
      API Documentation: https://polygon.io/docs/

      TD Ameritrade API:
      
      TD Ameritrade offers a powerful API (TDAmeritrade API) that provides access to a wide range of financial data, including options data.
      API Documentation: https://developer.tdameritrade.com/apis

      Alpaca API:
      
      Alpaca provides a brokerage API that allows access to real-time and historic market data, as well as trading capabilities, including options trading.
      API Documentation: https://alpaca.markets/docs/api-documentation/
