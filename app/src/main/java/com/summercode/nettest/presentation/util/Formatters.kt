package com.summercode.nettest.presentation.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val DATE_TIME_PATTERN = "dd.MM.yyyy HH:mm"

fun formatTimestamp(timestampMillis: Long): String =
    SimpleDateFormat(DATE_TIME_PATTERN, Locale.getDefault()).format(Date(timestampMillis))