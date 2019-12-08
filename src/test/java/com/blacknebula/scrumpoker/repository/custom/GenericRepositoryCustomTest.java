package com.blacknebula.scrumpoker.repository.custom;

import com.blacknebula.scrumpoker.ApplicationTest;
import com.blacknebula.scrumpoker.builders.VoteEntityBuilder;
import com.blacknebula.scrumpoker.entity.VoteEntity;
import com.blacknebula.scrumpoker.entity.def.VoteEntityDef;
import com.blacknebula.scrumpoker.repository.DAOResponse;
import com.blacknebula.scrumpoker.repository.VoteRepository;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author hazem
 */
public class GenericRepositoryCustomTest extends ApplicationTest {

    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * @verifies return unique entity with selected fields if Id exists
     * @see GenericRepositoryCustom#findOne(Serializable, List)
     */
    @Test
    public void findOne_shouldReturnUniqueEntityWithSelectedFieldsIfIdExists() throws Exception {
        // given
        final String username = "Leo";
        List<VoteEntity> existingVotes = ImmutableList.<VoteEntity>builder()
                .add(VoteEntityBuilder.builder()
                        .withSessionId("session-1")
                        .withStoryId("story-1")
                        .withUsername(username)
                        .withValue("1d")
                        .build())
                .add(VoteEntityBuilder.builder()
                        .withSessionId("session-1")
                        .withStoryId("story-2")
                        .withUsername(username)
                        .withValue("4h")
                        .build())
                .build();
        existingVotes = voteRepository.insert(existingVotes);

        // when
        final VoteEntity voteEntity = voteRepository.findOne(existingVotes.get(0).getVoteId(), Collections.singletonList(VoteEntityDef.USERNAME));

        // then
        Assertions.assertThat(voteEntity).isNotNull();
        Assertions.assertThat(voteEntity.getUsername()).isEqualTo(username);
        Assertions.assertThat(voteEntity.getSessionId()).isNull();
        Assertions.assertThat(voteEntity.getStoryId()).isNull();
        Assertions.assertThat(voteEntity.getValue()).isNull();
    }

    /**
     * @verifies return null id id is not valid
     * @see GenericRepositoryCustom#findOne(Serializable, List)
     */
    @Test
    public void findOne_shouldReturnNullIdIdIsNotValid() throws Exception {
        // when
        final VoteEntity foundVote = voteRepository.findOne("invalid_id", Lists.newArrayList());

        // then
        Assertions.assertThat(foundVote).isNull();
    }

    /**
     * @verifies return all documents with selected fields
     * @see GenericRepositoryCustom#search(java.util.List)
     */
    @Test
    public void search_shouldReturnAllDocumentsWithSelectedFields() throws Exception {
        // given
        final String username = "Leo";
        List<VoteEntity> existingVotes = ImmutableList.<VoteEntity>builder()
                .add(VoteEntityBuilder.builder()
                        .withSessionId("session-1")
                        .withStoryId("story-1")
                        .withUsername(username)
                        .withValue("1d")
                        .build())
                .add(VoteEntityBuilder.builder()
                        .withSessionId("session-1")
                        .withStoryId("story-2")
                        .withUsername(username)
                        .withValue("4h")
                        .build())
                .build();
        voteRepository.insert(existingVotes);

        // when
        final List<VoteEntity> foundVotes = voteRepository.search(Collections.singletonList(VoteEntityDef.USERNAME));

        // then
        Assertions.assertThat(foundVotes).hasSize(2);
        foundVotes.forEach(voteEntity -> {
            Assertions.assertThat(voteEntity.getUsername()).isEqualTo(username);
            Assertions.assertThat(voteEntity.getSessionId()).isNull();
            Assertions.assertThat(voteEntity.getStoryId()).isNull();
            Assertions.assertThat(voteEntity.getValue()).isNull();
        });

    }

    /**
     * @verifies return empty list if collection is empty
     * @see GenericRepositoryCustom#search(java.util.List)
     */
    @Test
    public void search_shouldReturnEmptyListIfCollectionIsEmpty() throws Exception {
        // when
        final List<VoteEntity> foundVotes = voteRepository.search(Collections.singletonList(VoteEntityDef.USERNAME));

        // then
        Assertions.assertThat(foundVotes).isEmpty();
    }

    /**
     * @verifies insert the object in the collection
     * @see GenericRepositoryCustom#create(Object)
     */
    @Test
    public void create_shouldInsertTheObjectInTheCollection() throws Exception {
        // given
        final String voteId = "voteId";
        final VoteEntity voteEntity = VoteEntityBuilder.builder()
                .withVoteId(voteId)
                .withSessionId("session-1")
                .withStoryId("story-2")
                .withUsername("Leo")
                .withValue("4h")
                .build();

        // when
        final VoteEntity createdVote = voteRepository.create(voteEntity);

        // then
        Assertions.assertThat(createdVote.getVoteId()).isNotNull();
        Assertions.assertThat(createdVote.getVoteId()).isEqualTo(voteId);
    }

    /**
     * @verifies generate a new id even if the id field is not set
     * @see GenericRepositoryCustom#create(Object)
     */
    @Test
    public void create_shouldGenerateANewIdEvenIfTheIdFieldIsNotSet() throws Exception {
        // given
        final VoteEntity voteEntity = VoteEntityBuilder.builder()
                .withSessionId("session-1")
                .withStoryId("story-2")
                .withUsername("Leo")
                .withValue("4h")
                .build();

        // when
        final VoteEntity createdVote = voteRepository.create(voteEntity);

        // then
        Assertions.assertThat(createdVote.getVoteId()).isNotNull();
    }

    /**
     * @verifies insert the object in the collection if Id is not set
     * @see GenericRepositoryCustom#upsert(Object)
     */
    @Test
    public void upsert_shouldInsertTheObjectInTheCollectionIfIdIsNotSet() throws Exception {
        // given
        final VoteEntity voteEntity = VoteEntityBuilder.builder()
                .withSessionId("session-1")
                .withStoryId("story-2")
                .withUsername("Leo")
                .withValue("4h")
                .build();

        // when
        voteRepository.upsert(voteEntity);

        // then
        final List<VoteEntity> createdVotes = voteRepository.findAll();
        Assertions.assertThat(createdVotes).hasSize(1);

    }

    /**
     * @verifies insert the object in the collection if Id is set but not found in the collection
     * @see GenericRepositoryCustom#upsert(Object)
     */
    @Test
    public void upsert_shouldInsertTheObjectInTheCollectionIfIdIsSetButNotFoundInTheCollection() throws Exception {
        // given
        final VoteEntity voteEntity = VoteEntityBuilder.builder()
                .withVoteId("voteId")
                .withSessionId("session-1")
                .withStoryId("story-2")
                .withUsername("Leo")
                .withValue("4h")
                .build();

        // when
        voteRepository.upsert(voteEntity);

        // then
        final List<VoteEntity> createdVotes = voteRepository.findAll();
        Assertions.assertThat(createdVotes).hasSize(1);
    }

    /**
     * @verifies update the object in the collection if Id is set and exists in the collection
     * @see GenericRepositoryCustom#upsert(Object)
     */
    @Test
    public void upsert_shouldUpdateTheObjectInTheCollectionIfIdIsSetAndExistsInTheCollection() throws Exception {
        // given
        final VoteEntity voteEntity = VoteEntityBuilder.builder()
                .withVoteId("voteId")
                .withSessionId("session-1")
                .withStoryId("story-2")
                .withUsername("Leo")
                .withValue("4h")
                .build();
        voteRepository.insert(voteEntity);

        // when
        final String newValue = "1d";
        voteEntity.setValue(newValue);
        voteRepository.upsert(voteEntity);

        // then
        final List<VoteEntity> createdVotes = voteRepository.findAll();
        Assertions.assertThat(createdVotes).hasSize(1);
        Assertions.assertThat(createdVotes.get(0).getValue()).isEqualTo(newValue);
    }

    /**
     * @verifies update field on selected document with given value
     * @see GenericRepositoryCustom#update(Serializable, String, Object)
     */
    @Test
    public void update_shouldUpdateFieldOnSelectedDocumentWithGivenValue() throws Exception {
        // given
        final String voteId = "voteId";
        final VoteEntity voteEntity = VoteEntityBuilder.builder()
                .withVoteId(voteId)
                .withSessionId("session-1")
                .withStoryId("story-2")
                .withUsername("Leo")
                .withValue("4h")
                .build();
        voteRepository.insert(voteEntity);

        // when
        final String newValue = "1d";
        final DAOResponse daoResponse = voteRepository.update(voteId, VoteEntityDef.VALUE, newValue);

        // then
        final List<VoteEntity> createdVotes = voteRepository.findAll();
        Assertions.assertThat(createdVotes).hasSize(1);
        Assertions.assertThat(createdVotes.get(0).getValue()).isEqualTo(newValue);
    }

    /**
     * @verifies update selected document with given values
     * @see GenericRepositoryCustom#update(Serializable, Map)
     */
    @Test
    public void update_shouldUpdateSelectedDocumentWithGivenValues() throws Exception {
        // given
        final String voteId = "voteId";
        final VoteEntity voteEntity = VoteEntityBuilder.builder()
                .withVoteId(voteId)
                .withSessionId("session-1")
                .withStoryId("story-2")
                .withUsername("Leo")
                .withValue("4h")
                .build();
        voteRepository.insert(voteEntity);

        // when
        final String newValue = "1d";
        final String newUserName = "Leonidas";
        final DAOResponse daoResponse = voteRepository.update(voteId, ImmutableMap.<String, Object>builder()
                .put(VoteEntityDef.VALUE, newValue)
                .put(VoteEntityDef.USERNAME, newUserName)
                .build());

        // then
        final List<VoteEntity> createdVotes = voteRepository.findAll();
        Assertions.assertThat(createdVotes).hasSize(1);
        Assertions.assertThat(createdVotes.get(0).getValue()).isEqualTo(newValue);
        Assertions.assertThat(createdVotes.get(0).getUsername()).isEqualTo(newUserName);
    }

    /**
     * @verifies increment numeric field with given value
     * @see GenericRepositoryCustom#increment(Serializable, String, Number)
     */
    @Test
    public void increment_shouldIncrementNumericFieldWithGivenValue() throws Exception {
        // given
        final String voteId = "voteId";
        final VoteEntity voteEntity = VoteEntityBuilder.builder()
                .withVoteId(voteId)
                .withSessionId("session-1")
                .withStoryId("story-2")
                .withUsername("Leo")
                .withValue("4h")
                .build();
        voteRepository.insert(voteEntity);
        final String counterAttribute = "counter";
        voteRepository.update(voteId, counterAttribute, 1);

        // when
        voteRepository.increment(voteId, counterAttribute, 1);

        // then
        final Query q = new Query();
        q.addCriteria(Criteria.where("_id").is(voteId));
        final Map voteAsMap = mongoTemplate.findOne(q, Map.class, "vote");
        Assertions.assertThat(voteAsMap.get(counterAttribute)).isEqualTo(2);
    }

}
