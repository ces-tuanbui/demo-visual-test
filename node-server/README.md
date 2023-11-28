# Visual Regression Test

Generate test suites that supports sharding

# Usage

## Generate spec files

```sh
npm run g:spec
```

## Initial snapshots

```sh
npx playwright test --shard=1/200 --update-snapshots
```

## Run test again to verify snapshot

```sh
npx playwright test --shard=1/200
```

## Generate JSON reporter

```sh
PLAYWRIGHT_JSON_OUTPUT_NAME=playwright-report/json-results.json npx playwright test --shard=1/200 --reporter=json
```
