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
	CREATE INDEX idx_education_country ON education using hash(country);
	--------------------reverse settings--------------------
	DROP INDEX idx_education_country;

8)
	CREATE INDEX idx_advertisement_date_posted ON advertisement ("datePosted");
	--------------------reverse settings--------------------
	DROP INDEX idx_advertisement_date_posted;

9)
	CREATE INDEX idx_msg_date_sent ON msg ("dateSent");
	--------------------reverse settings--------------------
	DROP INDEX idx_msg_date_sent;

10)
	CREATE INDEX idx_advertisement_date_posted ON advertisement ("datePosted");
	CREATE INDEX idx_msg_date_sent ON msg ("dateSent");
	--------------------reverse settings--------------------
	DROP INDEX idx_msg_date_sent, idx_advertisement_date_posted;

11)
	CREATE INDEX idx_msg_date ON msg ("dateSent" DESC); 
	CLUSTER msg USING idx_msg_date;
	--------------------reverse settings--------------------
	CREATE INDEX idx_msg_id ON msg("msgID");
	CLUSTER msg USING idx_msg_id;
	DROP INDEX idx_msg_date,idx_msg_id;

12)
	SET enable_seqscan = off;
	SET join_collapse_limit = 1;
	CREATE INDEX idx_edu_email_country ON education (email);
	--------------------reverse settings--------------------
	SET enable_seqscan = on;
	SET join_collapse_limit = 8;
	DROP INDEX idx_edu_email_country;

13)
	SET enable_seqscan = off;
	SET enable_sort = off;
	SET join_collapse_limit = 1;
	CREATE INDEX idx_edu_email_country ON education (email);
	--------------------reverse settings--------------------
	SET enable_seqscan = on;
	SET enable_sort = on;
	SET join_collapse_limit = 8;
	DROP INDEX idx_edu_email_country;

14)
	SET enable_seqscan = off;
	SET enable_sort = off;
	SET join_collapse_limit = 1;
	CREATE INDEX idx_msg_date_sent ON msg ("receiverEmail") INCLUDE("dateSent");
	--------------------reverse settings--------------------
	SET enable_seqscan = on;
	SET enable_sort = on;
	SET join_collapse_limit = 8;
	DROP INDEX idx_msg_date_sent;

15,16,17)
	SET join_collapse_limit = 1;
	SET enable_seqscan = off;
	SET enable_sort = off;
	CREATE INDEX idx_edu_email_country ON education (email) INCLUDE(country);
	CREATE INDEX idx_msg_date_sent ON msg ("receiverEmail") INCLUDE("dateSent");
	--------------------reverse settings--------------------
	SET enable_seqscan = on;
	SET enable_sort = on;
	SET join_collapse_limit = 8;
	DROP INDEX idx_edu_email_country, idx_msg_date_sent;



18)
	SET join_collapse_limit = 1;
	SET enable_seqscan = off;
	SET enable_sort = off;
	CREATE INDEX idx_advertisement_age ON "jobOffer" ("fromAge") INCLUDE("advertisementID");
	CREATE INDEX idx_advertisement_date ON advertisement ("datePosted");
	CREATE INDEX idx_edu_email_country ON education (email) INCLUDE(country);
	CREATE INDEX idx_msg_date_sent ON msg ("receiverEmail") INCLUDE("dateSent");
	--------------------reverse settings--------------------
	DROP INDEX idx_msg_date_sent, idx_edu_email_country,idx_advertisement_age, idx_advertisement_date;
	SET join_collapse_limit = 8;
	SET enable_seqscan = on;
	SET enable_sort = on;
*/