# Job Listings Portal

A PHP/HTML/CSS job listings website with a MySQL database and automated Selenium Java test scripts for end-to-end testing.

---

## Table of Contents

- [Prerequisites](#prerequisites)
- [Project Setup](#project-setup)
  - [Website Setup](#website-setup)
  - [Database Initialization](#database-initialization)
- [Running the Website](#running-the-website)
- [Selenium Java Testing](#selenium-java-testing)
- [Troubleshooting](#troubleshooting)
- [Useful Links](#useful-links)
- [Contact](#contact)

---

## Prerequisites

- [XAMPP](https://www.apachefriends.org/index.html) (or compatible Apache, PHP, and MySQL stack)
- [Git](https://git-scm.com/)
- Java (for Selenium tests)
- A Java IDE (e.g., Eclipse, IntelliJ)
- Google Chrome (for Selenium tests)

---

## Project Setup

### Website Setup

1. **Clone this repository:**
   ```sh
   git clone https://github.com/yohith-savanth05/Product-Filter-And-Search.git
   ```

2. **Move project files to your web root:**

   - For XAMPP, copy the project folder into:
     ```
     C:\xampp\htdocs\
     ```
     Example:
     ```
     C:\xampp\htdocs\job-listings-portal\
     ```

3. **Port Configuration (if using a non-default MySQL port):**
   - In the XAMPP Control Panel, click the **Config** button next to MySQL, open `my.ini`, and replace all instances of `3306` with your custom port (e.g., `3308`).
   - In the `phpMyAdmin` config file (`phpMyAdmin/config.inc.php`), adjust the host line:
     ```php
     $cfg['Servers'][$i]['host'] = '127.0.0.1:3308';
     ```

4. **Start Apache and MySQL** from the XAMPP Control Panel.

---

### Database Initialization

1. **Open phpMyAdmin:**  
   [http://localhost/phpmyadmin](http://localhost/phpmyadmin)

2. **Create a new database:**
   - Click **New**
   - Enter database name: `job_listings_db`
   - Click **Create**

3. **Create tables and insert sample data:**
   - Select the `job_listings_db` database.
   - Go to the **SQL** tab and run:

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

---

## Running the Website

1. In your browser, navigate to:
   ```
   http://localhost/your-project-folder/index.html
   ```
   Replace `your-project-folder` with the actual folder name.

2. The Job Listings Portal should be running locally.

---

## Selenium Java Testing

1. **Clone/download the Selenium test scripts:**  
   [https://github.com/yohith-savanth05/Yohith-Nishtha_SoftwareTesting.git](https://github.com/yohith-savanth05/Yohith-Nishtha_SoftwareTesting.git)

2. **Import the Java files** into your Java IDE (e.g., Eclipse).

3. **Add dependencies** to your project:
   - [Selenium Java JARs](https://www.selenium.dev/downloads/)
   - [JUnit 4](https://search.maven.org/artifact/junit/junit/4.13.2/jar)
   - [Hamcrest](https://search.maven.org/artifact/org.hamcrest/hamcrest-core/1.3/jar)

4. **Download [ChromeDriver](https://chromedriver.chromium.org/downloads)** matching your Chrome version.
   - Place it in a known directory (e.g., `C:\chromedriver\chromedriver.exe` or any directory of your choice).

5. **Edit your test code to point to the correct driver location:**
   ```java
   System.setProperty("webdriver.chrome.driver", "C:\\chromedriver\\chromedriver.exe");
   ```
   Adjust the path as needed for your system.

6. **Start XAMPP** (Apache and MySQL).

7. **Run your test files** (e.g., `JobListingTests.java`) as JUnit tests.
   - Chrome will launch, and the tests will interact with your website.

---

## Troubleshooting

- **Blank Page:**  
  - Check for PHP errors or database connection problems.

- **Database Connection Issues:**  
  - Ensure Apache and MySQL are running.
  - Verify credentials in your PHP files:
    - Host: `localhost`
    - Username: `root`
    - Password: *(leave blank unless changed)*

- **Selenium Errors:**  
  - Ensure all required JARs are in your classpath.
  - Check that ChromeDriver matches your browser version.
  - Make sure the ChromeDriver path is set correctly.

---

## Useful Links

- [XAMPP Download](https://www.apachefriends.org/index.html)
- [phpMyAdmin](http://localhost/phpmyadmin)
- [Selenium Downloads](https://www.selenium.dev/downloads/)
- [JUnit 4 JAR](https://search.maven.org/artifact/junit/junit/4.13.2/jar)
- [Hamcrest JAR](https://search.maven.org/artifact/org.hamcrest/hamcrest-core/1.3/jar)
- [ChromeDriver](https://chromedriver.chromium.org/downloads)

---

## Contact

For questions or support, please contact **yohithsavanth@gmail.com (or) 614nishtha@gmail.com**.
