# IntellijHybrisPlugin

## Installation
Download the plugin from [JetBrains repository](https://plugins.jetbrains.com/plugin/8046) or search it inside your intelliJ Version. 

## Usage
### Preparation
* Create a new run configuration
    * Look for 'Console script in hybris'
* Define the URL fpr the haC and your haC username and password
* You also can add parameters for FlexibleSearch

### Run a groovy script
* Create a new file that end with .groovy
* Write your script
* Run the script with your run configuration

### Run a BeanShell script
* Create a new file that end with .bsh or .beanshell
* Write your script
* Run the script with your run configuration

### Run a FlexibleSearch script
* Create a new file that end with .flex or .flexsearch
* Write your script
* Run the script with your run configuration

## Libaries
* Apache HTTP Client (v. 4.4)
* btc-ascii-table (v 1.0)
* junit (v 4.8.1 | just for tests)