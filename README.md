# NetTest

An Android network measurement app whose mode is driven by a remote config.
On first launch it fetches a JSON config, resolves the test mode and caches it
in DataStore; subsequent launches read the cache and make no network request.
The implemented test is the **speed test** — the ping mode shows a placeholder.

Kotlin · Compose · Coroutines/Flow · Koin · Ktor · DataStore · Room · MVVM ·
Clean Architecture (single module: `domain` / `data` / `presentation`).

Requires JDK 17 and **Android SDK Platform 37** — `compileSdk = 37` is forced by
`androidx.core:core-ktx:1.19.0`, which declares `minCompileSdk = 37`.

## Remote config

**Hosting.** The config lives in this repository and is served over GitHub raw:

```
https://raw.githubusercontent.com/LaffyGuy/net-test/main/config/app_config.json
```

Zero infrastructure, no SDK, and the config is versioned alongside the code.
Firebase Remote Config would be the production answer, but it pulls in the Google
Services plugin and a multi-megabyte SDK, and it manages its own cache — which
would have hidden the DataStore caching logic that is the point of the task.

Two quirks of this choice, both visible in the code:

* GitHub raw serves `.json` as `Content-Type: text/plain`, so Ktor's
  `ContentNegotiation` registers the JSON converter twice — for
  `application/json` and for `text/plain` (`NetworkModule.kt`).
* Responses are CDN-cached for 300 s, so a config change is not visible
  immediately. Irrelevant at runtime, since the config is read once per install.

**Format.** One meaningful parameter:

```json
{ "mode": "speed" }
```

Accepted values: `speed`, `ping` — case-insensitive, whitespace trimmed.

**Behaviour.**

| Situation | Result | Cached |
|---|---|---|
| Valid JSON, known `mode` | that mode | yes |
| Valid JSON, unknown `mode` (`"turbo"`) | default `speed` | **yes** |
| Valid JSON, `mode` missing or `null` | default `speed` | **yes** |
| No connection / timeout / 4xx / 5xx / not JSON | error screen + Retry | **no** |

Rows 2–3 are deliberate: an unknown value is not a failure. The network worked
and a config was received, so retrying returns the same thing — the default is
resolved and cached. A transport or parse failure means no config was received
at all, so nothing is written and the user can retry.

---

## Decisions and trade-offs

**Download source.** The assignment suggests `speed.cloudflare.com/__down`,
but Cloudflare's Browser Integrity Check returns 403 for non-browser clients,
including the `NetTest/1.0 (Android)` user agent this app sends. The
assignment allows a different source ("at least 100 MB, non-compressible"),
so I used OVH's public bandwidth-test file over HTTPS instead. Its contents
are never interpreted — only byte counts are used.

**Errors are sealed domain types, not exceptions.** `ConfigError` and
`SpeedTestError` cross layer boundaries; no Ktor or Room type ever does. 4xx and
5xx are separate cases because they call for different user actions — retrying a
4xx will not help.

**UI state is sealed, not a `data class` with flags.** `Loading | Error | Ready`
cannot represent "loading and errored"; exhaustive `when` makes the compiler
enforce that every case is handled.

**Navigation Compose 2, not Navigation 3.** Nav3 is stable and is where the
platform is heading, but its Koin integration is still alpha and its strengths
(adaptive multi-pane layouts, full back-stack control) are unused by two tabs.
Routes are type-safe `@Serializable` objects rather than strings.

**Ktor's OkHttp engine over CIO.** One more transitive dependency for the engine
with the most Android mileage; correct cancellation propagation to the socket
mattered more here than dependency count.

**Kotlin 2.4.10, not the 2.2.10 AGP 9 ships with.** Ktor 3.5, Koin 4.2 and
kotlinx-serialization 1.11 are built with Kotlin 2.3+ and their metadata cannot
be read by an older compiler. Note that AGP 9 has built-in Kotlin support, so
`org.jetbrains.kotlin.android` is deliberately not applied.

---

## Not done, and why

**Unit tests** — not required, and the weakest point of this submission. The
domain layer has no Android dependencies and would be easy to cover:
`AppMode.fromRaw()` against `"speed"` / `"turbo"` / `""` / `null`,
`ResolveAppModeUseCase` asserting the network is untouched when the cache is
populated, and `AppModeRepositoryImpl` asserting nothing is written on failure.
Koin's `checkModules()` would be worth having too, since Koin resolves at runtime.

**Parallel connections.** The app measures download over a single TCP
connection; commercial speed tests open several in parallel, which is why
results here read somewhat lower. Adding parallelism would mean a
thread-safe byte counter and cancelling several coroutines at once — extra
complexity in the trickiest part of the project, for accuracy the
assignment doesn't ask for.

**Ping test** — only one test was required. Speed was the deliberate choice: ICMP
is unavailable to a non-root Android app, so a "ping" would have been
`InetAddress.isReachable()` (which falls back to a TCP connect and returns false
on many networks) or a hand-rolled connect timer — harder to defend as a real
ping. The speed test also makes the cancellation requirement meaningful rather
than cosmetic.

**Config refresh** — the mode is cached forever, as specified. A real app would
want a TTL or a manual refresh.

**Dark theme, localisation, tablet layouts** — explicitly out of scope. The
Material 3 theme does follow the system dark mode and all strings live in
`strings.xml`, but neither was designed for.
