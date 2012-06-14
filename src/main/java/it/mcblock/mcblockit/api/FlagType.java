package it.mcblock.mcblockit.api;

/**
 * Determining flag type, description and full name
 *
 *
 * @author Adam Walker
 *
 *         Copyright 2012 Adam Walker
 *
 *         Licensed under the Apache License, Version 2.0 (the "License");
 *         you may not use this file except in compliance with the License.
 *         You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *         Unless required by applicable law or agreed to in writing, software
 *         distributed under the License is distributed on an "AS IS" BASIS,
 *         WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *         See the License for the specific language governing permissions and
 *         limitations under the License.
 *
 */

public enum FlagType {
    SCA, ASB, MI, GLD;

    public String getName () {
        switch (this) {
            case SCA:
                return "Suspected Compromised Account";
            case ASB:
                return "Automated Spam Bot";
            case MI:
                return "Malicious Intent";
            case GLD:
                return "Geographic Location Discrepancy";
        }
        return "Unknown";
    }

    public String getShortName () {
        return this.toString();
    }

    public String getDescription () {
        switch (this) {
            case SCA:
                return "This account is suspected of being compromised by a griefing team for malicious purposes.";
            case ASB:
                return "This account has been used for spamming servers with automated messages.";
            case MI:
                return "This account has been used to cause malicious harm to the Minecraft community (See MCBlock.it for more info.)";
            case GLD:
                return "This account has been logged into from a location MCBlockIt is not familiar with.";
        }
        return "Unknown";
    }
}
