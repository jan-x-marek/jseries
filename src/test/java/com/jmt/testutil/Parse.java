/* 
   Copyright 2017 Jan Marek
   
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package com.jmt.testutil;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class Parse {

    public static Instant date(String s) throws DateTimeException {
        return LocalDate.from(createFormatter("dd/MM/yyyy").parse(s)).atStartOfDay(ZoneId.of("UTC")).toInstant();
    }

    public static Instant dateTime(String s) {
        return Instant.from(createFormatter("dd/MM/yyyy HH:mm:ss z").parse(s));
    }

    private static DateTimeFormatter createFormatter(String pattern) {
        return new DateTimeFormatterBuilder()
                .appendPattern(pattern)
                .toFormatter()
                .withZone(ZoneId.of("UTC"));
    }
}
