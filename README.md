## Verify page status using resilience4j

Let’s say we have a list of 10 URLs in a text file. 

We need to write a simple application that hits these pages over HTTP and 
verifies that we receive 200 OK status codes.

If we don’t receive 200OK on the first attempt, the application should do 4 additional retries.
Every retry should be on a 5-second interval.

### References
- https://resilience4j.readme.io/
- https://httpstat.us

---

## Contributors

Thank you to everyone who has helped this project. Your code, reviews, issues, and pull requests are appreciated.

- [@YauhenBichel](https://github.com/YauhenBichel)

See the [full contributor graph](https://github.com/YauhenBichel/Retry-with-resilience4j/graphs/contributors).
