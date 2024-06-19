--SET max_parallel_workers_per_gather  = 0;
--VACUUM ANALYZE;
/* --first query
SELECT edu."eduLevel", COUNT(edu."eduLevel") AS member_count
FROM 
	education edu JOIN
	msg ON(edu.email = "receiverEmail") JOIN
	(
		SELECT a.email
		FROM advertisement a JOIN "jobOffer" USING("advertisementID")
		WHERE "fromAge" > 21 AND "fromAge" < 30 AND "datePosted" >= (CURRENT_DATE - INTERVAL'6 months')
		GROUP BY a.email
		HAVING COUNT(a.email) >= 2
	)adv USING(email) 
WHERE 
	edu.country = 'Canada' AND
	"dateSent" >= (CURRENT_DATE - INTERVAL'6 months')
GROUP BY edu."eduLevel"
*/

/* -- second query
SELECT edu."eduLevel", COUNT(edu."eduLevel") AS member_count
FROM 
	msg JOIN
	(
		SELECT a.email
		FROM advertisement a JOIN "jobOffer" USING("advertisementID")
		WHERE "fromAge" > 21 AND "fromAge" < 30 AND "datePosted" >= (CURRENT_DATE - INTERVAL'6 months')
		GROUP BY a.email
		HAVING COUNT(a.email) >= 2
	)adv ON(email = "receiverEmail") JOIN
	education edu ON(edu.email = msg."receiverEmail")
WHERE 
	edu.country = 'Canada' AND
	"dateSent" >= (CURRENT_DATE - INTERVAL'6 months')
GROUP BY edu."eduLevel"
*/

 -- third query
--EXPLAIN ANALYZE
SELECT edu."eduLevel", COUNT(edu."eduLevel") AS member_count
FROM 
	(
		SELECT a.email
		FROM advertisement a JOIN "jobOffer" USING("advertisementID")
		WHERE "fromAge" > 21 AND "fromAge" < 30 AND "datePosted" >= (CURRENT_DATE - INTERVAL'6 months')
		GROUP BY a.email
		HAVING COUNT(a.email) >= 2
	)adv JOIN
	education edu USING(email) JOIN
	msg ON("receiverEmail" = email)	
WHERE 
	edu.country = 'Canada' AND
	"dateSent" >= (CURRENT_DATE - INTERVAL'6 months')
GROUP BY edu."eduLevel" 


/*	
1)
	everything on

2)
	SET enable_hashjoin = off;
	---------reverse settings---------
	SET enable_hashjoin = on;

3)
	SET enable_hashjoin = off;
	SET enable_mergejoin = off;
	---------reverse settings---------
	SET enable_hashjoin = on;
	SET enable_mergejoin = on;

4,5,6)
	FORCE JOIN SEQ
	SET join_collapse_limit = 1;
	---------reverse settings---------
	SET join_collapse_limit = 8;

7)
	SET join_collapse_limit = 1;
	CREATE INDEX idx_education_country ON education(country);
	--------------------reverse settings--------------------
	DROP INDEX idx_education_country;
	SET join_collapse_limit = 8;

8)
	SET join_collapse_limit = 1;
	CREATE INDEX idx_msg_email ON msg ("receiverEmail");
	--------------------reverse settings--------------------
	DROP INDEX idx_msg_email;
	SET join_collapse_limit = 8;

9)
	SET join_collapse_limit = 1;
	CREATE INDEX idx_advertisement_date_posted ON advertisement ("datePosted");
	CREATE INDEX idx_msg_email ON msg ("receiverEmail");
	--------------------reverse settings--------------------
	DROP INDEX idx_msg_email, idx_advertisement_date_posted;
	SET join_collapse_limit = 8;

10)
	SET join_collapse_limit = 1;
	CREATE INDEX idx_msg_email ON msg ("receiverEmail"); 
	CREATE INDEX idx_advertisement_date_posted ON advertisement ("datePosted");
	CLUSTER msg USING idx_msg_email;
	--------------------reverse settings--------------------
	CREATE INDEX idx_msg_id ON msg("msgID");
	CLUSTER msg USING idx_msg_id;
	DROP INDEX idx_msg_email,idx_msg_id,idx_advertisement_date_posted;
	SET join_collapse_limit = 8;

11)
	SET enable_seqscan = off;
	SET join_collapse_limit = 1;
	CREATE INDEX idx_msg_email ON msg ("receiverEmail"); 
	CREATE INDEX idx_advertisement_date_posted ON advertisement ("datePosted");
	CREATE INDEX idx_edu_email ON education (email);
	--------------------reverse settings--------------------
	SET enable_seqscan = on;
	SET join_collapse_limit = 8;
	DROP INDEX idx_edu_email,idx_msg_email,idx_advertisement_date_posted;

12)
	SET enable_sort = off;
	SET enable_seqscan = off;
	SET join_collapse_limit = 1;
	CREATE INDEX idx_msg_email ON msg ("receiverEmail"); 
	CREATE INDEX idx_advertisement_date_posted ON advertisement ("datePosted");
	CREATE INDEX idx_edu_email ON education (email);
	--------------------reverse settings--------------------
	SET enable_seqscan = on;
	SET enable_sort = on;
	SET join_collapse_limit = 8;
	DROP INDEX idx_edu_email,idx_msg_email,idx_advertisement_date_posted;

13)
	SET enable_sort = off;
	SET enable_seqscan = off;
	SET join_collapse_limit = 1;
	CREATE INDEX idx_msg_email ON msg ("receiverEmail"); 
	CREATE INDEX idx_advertisement_date_posted ON advertisement ("datePosted") INCLUDE("advertisementID", email);
	CREATE INDEX idx_edu_email ON education (email);
	--------------------reverse settings--------------------
	SET enable_seqscan = on;
	SET enable_sort = on;
	SET join_collapse_limit = 8;
	DROP INDEX idx_edu_email,idx_msg_email,idx_advertisement_date_posted;

14)
	SET enable_bitmapscan = off;
	SET enable_sort = off;
	SET enable_seqscan = off;
	SET join_collapse_limit = 1;
	CREATE INDEX idx_msg_email ON msg ("receiverEmail"); 
	CREATE INDEX idx_advertisement_date_posted ON advertisement ("datePosted" DESC) INCLUDE("advertisementID", email);
	CREATE INDEX idx_edu_email ON education (email);
	CREATE INDEX idx_advertisement_age ON "jobOffer" ("fromAge") INCLUDE("advertisementID");
	--------------------reverse settings--------------------
	SET enable_seqscan = on;
	SET enable_sort = on;
	SET join_collapse_limit = 8;
	DROP INDEX idx_edu_email,idx_msg_email,idx_advertisement_date_posted,idx_advertisement_age;
*/
