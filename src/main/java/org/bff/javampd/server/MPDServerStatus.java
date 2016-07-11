package org.bff.javampd.server;

import com.google.inject.Inject;
import org.bff.javampd.command.CommandExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;

/**
 * @author bill
 */
public class MPDServerStatus implements ServerStatus {
    private static final Logger LOGGER = LoggerFactory.getLogger(MPDServerStatus.class);

    private ServerProperties serverProperties;
    private CommandExecutor commandExecutor;

    private enum TimeType {
        ELAPSED(0),
        TOTAL(1);

        private int index;

        TimeType(int index) {
            this.index = index;
        }

        public int getIndex() {
            return this.index;
        }
    }

    @Inject
    public MPDServerStatus(ServerProperties serverProperties,
                           CommandExecutor commandExecutor) {
        this.serverProperties = serverProperties;
        this.commandExecutor = commandExecutor;
    }

    /**
     * Returns the current status of the requested status element.
     * See {@link Status} for a list of possible items returned
     * by getStatus.  If the status isnt part of the response
     * "" is returned.
     *
     * @param status the status desired
     * @return the desired status information
     */
    protected String getStatus(Status status) {
        List<String> respList = commandExecutor.sendCommand(serverProperties.getStatus());

        for (String line : respList) {
            if (line.startsWith(status.getStatusPrefix())) {
                return line.substring(status.getStatusPrefix().length()).trim();
            }
        }
        LOGGER.warn("Response did not contain status {}", status.getStatusPrefix());
        return "";
    }

    @Override
    public Collection<String> getStatus() {
        return commandExecutor.sendCommand(serverProperties.getStatus());
    }

    @Override
    public int getPlaylistVersion() {
        String version = getStatus(Status.PLAYLIST);
        try {
            return Integer.parseInt("".equals(version) ? "0" : version);
        } catch (NumberFormatException nfe) {
            LOGGER.error("Could not format playlist version response {}", version, nfe);
            return 0;
        }
    }

    @Override
    public String getState() {
        return getStatus(Status.STATE);
    }

    @Override
    public int getXFade() {
        String xFade = getStatus(Status.XFADE);
        try {
            return Integer.parseInt("".equals(xFade) ? "0" : xFade);
        } catch (NumberFormatException nfe) {
            LOGGER.error("Could not format xfade response {}", xFade, nfe);
            return 0;
        }
    }

    @Override
    public String getAudio() {
        return getStatus(Status.AUDIO);
    }

    @Override
    public boolean isError() {
        return !"".equals(getStatus(Status.ERROR));
    }

    @Override
    public String getError() {
        return getStatus(Status.ERROR);
    }

    @Override
    public long getElapsedTime() {
        return lookupTime(TimeType.ELAPSED);
    }

    @Override
    public long getElapsedTimeMillis() {
        return Math.round(Double.parseDouble(getStatus(Status.ELAPSED)) * 1000);
    }

    @Override
    public long getTotalTime() {
        return lookupTime(TimeType.TOTAL);
    }

    @Override
    public int getBitrate() {
        String bitrate = getStatus(Status.BITRATE);
        try {
            return Integer.parseInt("".equals(bitrate) ? "0" : bitrate);
        } catch (NumberFormatException nfe) {
            LOGGER.error("Could not format bitrate response {}", bitrate, nfe);
            return 0;
        }
    }

    @Override
    public int getVolume() {
        String volume = getStatus(Status.VOLUME);
        try {
            return Integer.parseInt("".equals(volume) ? "0" : volume);
        } catch (NumberFormatException nfe) {
            LOGGER.error("Could not format volume response {}", volume, nfe);
            return 0;
        }
    }

    @Override
    public boolean isRepeat() {
        return "1".equals(getStatus(Status.REPEAT));
    }

    @Override
    public boolean isRandom() {
        return "1".equals(getStatus(Status.RANDOM));
    }

    private long lookupTime(TimeType type) {
        String time = getStatus(Status.TIME);

        if ("".equals(time) || !time.contains(":")) {
            return 0;
        } else {
            try {
                return Integer.parseInt(time.trim().split(":")[type.getIndex()]);
            } catch (NumberFormatException nfe) {
                LOGGER.error("Could not format time {}", time, nfe);
                return 0;
            }
        }
    }
}
