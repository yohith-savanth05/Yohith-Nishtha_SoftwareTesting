Job Listings Portal – Setup and Testing Instructions

Welcome! This document provides detailed instructions to set up, run, and test the Job Listings Portal project on your local machine.  
The project includes a PHP/HTML/CSS website (with MySQL database) and Selenium Java test scripts for end-to-end testing.

---

## 1. Prerequisites

- **Download and Install [XAMPP](https://www.apachefriends.org/index.html)**
  - Run XAMPP as Administrator.
- Beside MySQL there is a configs button under that there is a file name my.ini, under that change all 3306 into 3308 (as our port is different)
- then go to where you have saved your xampp folder inside that there is a file named phpAdmin inside that open config.inc and on line 27 and change the end of the port and add 3308, for example: $cfg['Servers'][$i]['host'] = '127.0.0.1:3308';
  - Start **Apache** and **MySQL** services from the XAMPP Control Panel.

---

## 2. Setting Up the Website

### a. Place Project Files

1. **Download the provided project ZIP file.**
2. **Extract** the contents.
3. **Copy the entire project folder** (containing your HTML, CSS, PHP, JS, etc.) into:
   ```
   C:\xampp\htdocs\
   ```
   For example, you should have:
   ```
   C:\xampp\htdocs\your-project-folder\
   ```

### b. Create the Database

1. **Open phpMyAdmin**:  
   In your browser, go to [http://localhost/phpmyadmin](http://localhost/phpmyadmin)
2. **Create a new database**:  
   - Click on **New** in the left sidebar.
   - Enter the database name: `job_listings_db`
   - Click **Create**.

3. **Create Tables and Fields**
   - With the new database selected, go to the **SQL** tab.
   - Copy and paste the following SQL commands to create your main table and insert sample data:

   ```sql
CREATE TABLE `jobs` (
  `title` text NOT NULL,
  `company` text NOT NULL DEFAULT 'Optimum Solutions',
  `location` varchar(50) NOT NULL,
  `jobImg` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


INSERT INTO `jobs` (`title`, `company`, `location`, `jobImg`) VALUES
('Dev Ops', 'Optimum Solutions', 'Singapore', 'Media/Job-listing_icons/devOps.svg'),
('Creative Graphic Designer', 'Design Studio', 'Manchester, UK', 'Media/Job-listing_icons/manageSearch.svg'),
('Support Specialist', 'Customer First', 'Berlin, Germany', 'Media/Job-listing_icons/smtg.svg');
COMMIT;
   ```
   - Click **Go**.

---

## 3. Running the Website

1. In your browser, go to:  
   ```
http://localhost/Product%20Filter%20And%20Search/index.html
   ```

2. You should now see the Job Listings Portal running locally.

---

## 4. Selenium Java Testing 

### a. Download and Set Up

1.	**Clone or download** the Selenium test Java files from this repository:  
https://github.com/yohith-savanth05/Yohith-Nishtha_SoftwareTesting.git


2. **Open Eclipse (or your preferred Java IDE)**

3. **Import the Java files** into your project/workspace.

4. **Add Dependencies to Your Project:**
   - Download and add the following JARs to your project’s build path:
     - [Selenium Java](https://www.selenium.dev/downloads/)
     - [JUnit 4](https://search.maven.org/artifact/junit/junit/4.13.2/jar)
     - [Hamcrest](https://search.maven.org/artifact/org.hamcrest/hamcrest-core/1.3/jar)
Change the classpaths of each of the dependencies according to your device.

5. **Download ChromeDriver**
   - Download the correct [ChromeDriver](https://chromedriver.chromium.org/downloads) for your Chrome version.
   - Place it in a known folder, e.g., `C:\chromedriver\chromedriver.exe`

6. **Edit the path** in your Java test code:
   ```java
   System.setProperty("webdriver.chrome.driver", "C:\\chromedriver\\chromedriver.exe");
   ```

### b. Running the Selenium Tests

1. **Start XAMPP** (if not running already).
2. **Run your Java test files** (e.g., `JobListingTests.java`) as a JUnit test in Eclipse.
3. The automated tests will launch Chrome, interact with your website, and check its functionality.

---

## 5. Additional Notes

- **Default Database Credentials**  
  The PHP site uses default XAMPP settings:  
  - Host: `localhost`
  - Username: `root`
  - Password: *(blank)*  
  If you change these, update your PHP connection code accordingly.

- **Troubleshooting**
  - Ensure Apache and MySQL are running.
  - If you see a blank page, check for PHP errors or database connection issues.
  - For Selenium, ensure all required JARs are in your classpath.

---

## 6. Useful Links

- [XAMPP Download](https://www.apachefriends.org/index.html)
- [phpMyAdmin](http://localhost/phpmyadmin)
- [Selenium Downloads](https://www.selenium.dev/downloads/)
- [JUnit Jar](https://search.maven.org/artifact/junit/junit/4.13.2/jar)
- [Hamcrest Jar](https://search.maven.org/artifact/org.hamcrest/hamcrest-core/1.3/jar)
- [ChromeDriver](https://chromedriver.chromium.org/downloads)

---

**If you have any questions or issues, please contact [your name/email here].**
