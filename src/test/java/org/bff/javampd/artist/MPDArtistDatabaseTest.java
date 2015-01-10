package org.bff.javampd.artist;

import org.bff.javampd.database.TagLister;
import org.bff.javampd.genre.MPDGenre;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MPDArtistDatabaseTest {
    @Mock
    private TagLister tagLister;

    @InjectMocks
    private MPDArtistDatabase artistDatabase;

    @Test
    public void testListAllArtists() throws Exception {
        MPDArtist testArtist = new MPDArtist("testName");

        List<String> mockReturn = new ArrayList<>();
        mockReturn.add(testArtist.getName());

        when(tagLister
                .list(TagLister.ListType.ARTIST))
                .thenReturn(mockReturn);

        List<MPDArtist> artists = new ArrayList<>(artistDatabase.listAllArtists());
        assertEquals(1, artists.size());
        assertEquals(testArtist, artists.get(0));
    }

    @Test
    public void testListArtistsByGenre() throws Exception {
        MPDGenre testGenre = new MPDGenre("testGenreName");

        String testArtistName = "testArtist";

        List<String> mockGenreList = new ArrayList<>();
        mockGenreList.add(TagLister.ListType.GENRE.getType());
        mockGenreList.add(testGenre.getName());

        List<String> mockReturnGenreList = new ArrayList<>();
        mockReturnGenreList.add(testArtistName);

        List<String> mockReturnArtist = new ArrayList<>();
        mockReturnArtist.add(testArtistName);

        when(tagLister
                .list(TagLister.ListType.ARTIST, mockGenreList))
                .thenReturn(mockReturnArtist);

        MPDArtist testArtist = new MPDArtist(testArtistName);

        List<MPDArtist> artists = new ArrayList<>(artistDatabase.listArtistsByGenre(testGenre));
        assertEquals(1, artists.size());
        assertEquals(testArtist, artists.get(0));
    }

    @Test
    public void testListArtistByName() throws Exception {
        String testArtistName = "testArtist";

        List<String> mockReturnName = new ArrayList<>();
        mockReturnName.add(TagLister.ListType.ARTIST.getType());
        mockReturnName.add(testArtistName);

        List<String> mockReturnArtist = new ArrayList<>();
        mockReturnArtist.add(testArtistName);

        when(tagLister
                .list(TagLister.ListType.ARTIST, mockReturnName))
                .thenReturn(mockReturnArtist);

        MPDArtist testArtist = new MPDArtist(testArtistName);

        List<MPDArtist> artists = new ArrayList<>();
        artists.add(artistDatabase.listArtistByName(testArtistName));
        assertEquals(1, artists.size());
        assertEquals(testArtist, artists.get(0));
    }
}