package it.mcblock.mcblockit.api.queue;

/**
 * An individual ban item to be sent to the API after successful banning on the server.
 * No getter methods as this is handled entirely by Gson
 * 
 * @author Matt Baxter
 * 
 *         Copyright 2012 Matt Baxter
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
@SuppressWarnings("unused")
public class BanItem extends QueueItem {
    private final String name;
    private final String admin;
    private final int type;//0=local,1=global,2=EAC
    private final String reason;

    public BanItem(String name, String admin, int type, String reason) {
        this.name = name;
        this.admin = admin;
        this.type = type;
        this.reason = reason;
    }

}
