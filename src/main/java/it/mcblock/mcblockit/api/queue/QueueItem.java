package it.mcblock.mcblockit.api.queue;

import java.util.Date;

/**
 * An individual item to be sent to the API after successful action on the server.
 * No getter methods as this is handled entirely by Gson.
 * Comparison method provided for sorting in the queue.
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
public abstract class QueueItem implements Comparable<QueueItem> {
    private final long timestamp;

    public QueueItem() {
        this.timestamp = (new Date()).getTime() / 1000;
    }

    public QueueItem(long time) {
        this.timestamp = 0;
    }

    @Override
    public int compareTo(QueueItem other) {
        return (int) (this.timestamp - other.timestamp);
    }
}
