package org.bff.javampd.song;

import org.bff.javampd.command.CommandExecutor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MPDSongSearcherTest {

    private SongSearcher songSearcher;
    private CommandExecutor mockedCommandExecuter;
    private SongConverter mockedSongConverter;
    private SearchProperties searchProperties;

    @Captor
    private ArgumentCaptor<String> commandArgumentCaptor;
    @Captor
    private ArgumentCaptor<String[]> paramArgumentCaptor;

    @Before
    public void setup() {
        searchProperties = new SearchProperties();
        mockedSongConverter = mock(SongConverter.class);
        mockedCommandExecuter = mock(CommandExecutor.class);
        songSearcher = new MPDSongSearcher(searchProperties,
                mockedCommandExecuter,
                mockedSongConverter);
    }

    @Test
    public void testSearch() throws Exception {
        String searchCriteria = "testSearch";
        SongSearcher.ScopeType scopeType = SongSearcher.ScopeType.ALBUM;
        MPDSong testSong = new MPDSong("testFile", "testName");

        List<MPDSong> testSongList = new ArrayList<>();
        testSongList.add(testSong);

        when(mockedCommandExecuter.sendCommand(searchProperties.getSearch(),
                generateParams(scopeType, searchCriteria))).thenReturn(new ArrayList<>());
        when(mockedSongConverter.convertResponseToSong(new ArrayList<>())).thenReturn(testSongList);

        List<MPDSong> songList = new ArrayList<>(songSearcher.search(scopeType, searchCriteria));

        verify(mockedCommandExecuter).sendCommand(commandArgumentCaptor.capture(), paramArgumentCaptor.capture());

        assertEquals(testSongList.size(), songList.size());
        assertEquals(searchProperties.getSearch(), commandArgumentCaptor.getValue());
        assertEquals(Arrays.asList(generateParams(scopeType, searchCriteria)), paramArgumentCaptor.getAllValues());
        assertEquals(testSongList.get(0), songList.get(0));
    }

    @Test
    public void testSearchWindowed() throws Exception {
        String searchCriteria = "testSearch";
        int start = 1;
        int end = 5;

        SongSearcher.ScopeType scopeType = SongSearcher.ScopeType.ARTIST;
        MPDSong testSong = new MPDSong("testFile", "testName");

        List<MPDSong> testSongList = new ArrayList<>();
        testSongList.add(testSong);


        when(mockedCommandExecuter.sendCommand(searchProperties.getSearch(),
                addWindowedParams(generateParams(scopeType, searchCriteria), start, end)))
                .thenReturn(new ArrayList<>());
        when(mockedSongConverter.convertResponseToSong(new ArrayList<>())).thenReturn(testSongList);

        List<MPDSong> songList = new ArrayList<>(songSearcher.search(scopeType, searchCriteria, start, end));
        verify(mockedCommandExecuter).sendCommand(commandArgumentCaptor.capture(), paramArgumentCaptor.capture());

        assertEquals(testSongList.size(), songList.size());
        assertEquals(searchProperties.getSearch(), commandArgumentCaptor.getValue());
        assertEquals(Arrays.asList(addWindowedParams(generateParams(scopeType, searchCriteria), start, end)),
                paramArgumentCaptor.getAllValues());
        assertEquals(testSongList.get(0), songList.get(0));
    }

    @Test
    public void testFind() throws Exception {
        String searchCriteria = "testSearch";
        SongSearcher.ScopeType scopeType = SongSearcher.ScopeType.ALBUM;
        MPDSong testSong = new MPDSong("testFile", "testName");

        List<MPDSong> testSongList = new ArrayList<>();
        testSongList.add(testSong);

        when(mockedCommandExecuter.sendCommand(searchProperties.getFind(),
                generateParams(scopeType, searchCriteria))).thenReturn(new ArrayList<>());
        when(mockedSongConverter.convertResponseToSong(new ArrayList<>())).thenReturn(testSongList);

        List<MPDSong> songList = new ArrayList<>(songSearcher.find(scopeType, searchCriteria));

        verify(mockedCommandExecuter).sendCommand(commandArgumentCaptor.capture(), paramArgumentCaptor.capture());

        assertEquals(testSongList.size(), songList.size());
        assertEquals(searchProperties.getFind(), commandArgumentCaptor.getValue());
        assertEquals(Arrays.asList(generateParams(scopeType, searchCriteria)), paramArgumentCaptor.getAllValues());
        assertEquals(testSongList.get(0), songList.get(0));
    }


    @Test
    public void testFindNoCriteria() throws Exception {
        SongSearcher.ScopeType scopeType = SongSearcher.ScopeType.ALBUM;
        MPDSong testSong = new MPDSong("testFile", "testName");

        List<MPDSong> testSongList = new ArrayList<>();
        testSongList.add(testSong);

        when(mockedCommandExecuter.sendCommand(searchProperties.getFind(),
                generateParams(scopeType, ""))).thenReturn(new ArrayList<>());
        when(mockedSongConverter.convertResponseToSong(new ArrayList<>())).thenReturn(testSongList);

        List<MPDSong> songList = new ArrayList<>(songSearcher.find(scopeType, ""));

        verify(mockedCommandExecuter).sendCommand(commandArgumentCaptor.capture(), paramArgumentCaptor.capture());

        assertEquals(testSongList.size(), songList.size());
        assertEquals(searchProperties.getFind(), commandArgumentCaptor.getValue());
        assertEquals(Arrays.asList(generateParams(scopeType, "")), paramArgumentCaptor.getAllValues());
        assertEquals(testSongList.get(0), songList.get(0));
    }

    @Test
    public void testFindNullCriteria() throws Exception {
        SongSearcher.ScopeType scopeType = SongSearcher.ScopeType.ANY;
        MPDSong testSong = new MPDSong("testFile", "testName");

        List<MPDSong> testSongList = new ArrayList<>();
        testSongList.add(testSong);

        when(mockedCommandExecuter.sendCommand(searchProperties.getFind(),
                generateParams(scopeType, null))).thenReturn(new ArrayList<>());
        when(mockedSongConverter.convertResponseToSong(new ArrayList<>())).thenReturn(testSongList);

        List<MPDSong> songList = new ArrayList<>(songSearcher.find(scopeType, null));

        verify(mockedCommandExecuter).sendCommand(commandArgumentCaptor.capture(), paramArgumentCaptor.capture());

        assertEquals(testSongList.size(), songList.size());
        assertEquals(searchProperties.getFind(), commandArgumentCaptor.getValue());
        assertEquals(Arrays.asList(generateParams(scopeType, null)), paramArgumentCaptor.getAllValues());
        assertEquals(testSongList.get(0), songList.get(0));
    }

    @Test
    public void testFindWindowed() throws Exception {
        String searchCriteria = "testSearch";
        int start = 1;
        int end = 5;

        SongSearcher.ScopeType scopeType = SongSearcher.ScopeType.ARTIST;
        MPDSong testSong = new MPDSong("testFile", "testName");

        List<MPDSong> testSongList = new ArrayList<>();
        testSongList.add(testSong);

        when(mockedCommandExecuter.sendCommand(searchProperties.getFind(),
                addWindowedParams(generateParams(scopeType, searchCriteria), start, end)))
                .thenReturn(new ArrayList<>());
        when(mockedSongConverter.convertResponseToSong(new ArrayList<>())).thenReturn(testSongList);

        List<MPDSong> songList = new ArrayList<>(songSearcher.find(scopeType, searchCriteria, start, end));
        verify(mockedCommandExecuter).sendCommand(commandArgumentCaptor.capture(), paramArgumentCaptor.capture());

        assertEquals(testSongList.size(), songList.size());
        assertEquals(searchProperties.getFind(), commandArgumentCaptor.getValue());
        assertEquals(Arrays.asList(addWindowedParams(generateParams(scopeType, searchCriteria), start, end)),
                paramArgumentCaptor.getAllValues());
        assertEquals(testSongList.get(0), songList.get(0));
    }

    private String[] addWindowedParams(String[] params,
                                       int start,
                                       int end) {
        String[] paramList = Arrays.copyOf(params, params.length + 2);
        paramList[params.length] = searchProperties.getWindow();
        paramList[params.length + 1] = start + "." + end;

        return paramList;
    }

    private String[] generateParams(SongSearcher.ScopeType scopeType,
                                    String criteria) {
        String[] paramList;

        if (criteria != null && !"".equals(criteria)) {
            paramList = new String[2];
            paramList[1] = criteria;
        } else {
            paramList = new String[1];
        }
        paramList[0] = scopeType.getType();

        return paramList;
    }
}