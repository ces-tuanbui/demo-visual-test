import { test, expect } from "@playwright/test";

test(`Screenshot for https://www.veracode.com/blog/2014/09/a-guide-to-static-testing-of-web-apps-no-running-required`, async ({ page }) => {
  const url = "https://www.veracode.com/blog/2014/09/a-guide-to-static-testing-of-web-apps-no-running-required";

  // go to URL
  await page.goto(url);

  // click on "Accept All Cookies"
  await page.waitForSelector("#onetrust-button-group-parent", {
    timeout: 3000,
  });
  const button = await page.$("#onetrust-button-group-parent");
  if (button) {
    await button.click();
  }

  const name = `${url.replace(/[^a-zA-Z0-9]/g, "_")}.png`;
  const screenshotPath = `screenshots/${name}`;

  await page.screenshot({ path: screenshotPath, fullPage: true });

  await expect(page).toHaveScreenshot(name);
});
