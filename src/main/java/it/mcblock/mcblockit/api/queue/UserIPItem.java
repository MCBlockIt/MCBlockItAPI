package it.mcblock.mcblockit.api.queue;

import java.util.HashMap;

/**
 * A group item to be sent to the API every minute of recently connected users and their IPs
 * No getter methods as this is handled entirely by Gson.
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

public class UserIPItem extends QueueItem {

    private final HashMap<String, String> userIPList;

    public UserIPItem (HashMap<String, String> list) {
        this.userIPList = list;
    }
}
