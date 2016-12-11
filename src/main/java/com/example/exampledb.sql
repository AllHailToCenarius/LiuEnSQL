--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.5
-- Dumped by pg_dump version 9.5.5

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: category; Type: TABLE; Schema: public; Owner: dbuser
--

CREATE TABLE category (
    categoryname character varying(100) NOT NULL
);


ALTER TABLE category OWNER TO dbuser;

--
-- Name: photo; Type: TABLE; Schema: public; Owner: dbuser
--

CREATE TABLE photo (
    id character varying(100) NOT NULL,
    title character varying(100),
    author character varying(100),
    tags character varying(1000),
    essay character varying(500)
);


ALTER TABLE photo OWNER TO dbuser;

--
-- Name: photocomments; Type: TABLE; Schema: public; Owner: dbuser
--

CREATE TABLE photocomments (
    photoid character varying(100),
    username character varying(100),
    date date,
    readercomment character varying(1000)
);


ALTER TABLE photocomments OWNER TO dbuser;

--
-- Name: photodetail; Type: TABLE; Schema: public; Owner: dbuser
--

CREATE TABLE photodetail (
    photoid character varying(100) NOT NULL,
    datetaken character varying(100),
    dateuploaded character varying(100),
    location character varying(100),
    camera character varying(100),
    focallength character varying(100),
    shutterspeed character varying(100),
    aperture character varying(100),
    iso character varying(100),
    copyright character varying(100)
);


ALTER TABLE photodetail OWNER TO dbuser;

--
-- Name: photoxcategory; Type: TABLE; Schema: public; Owner: dbuser
--

CREATE TABLE photoxcategory (
    photoid character varying(100),
    category character varying(100)
);


ALTER TABLE photoxcategory OWNER TO dbuser;

--
-- Name: users; Type: TABLE; Schema: public; Owner: dbuser
--

CREATE TABLE users (
    username character varying(100) NOT NULL,
    password character varying(100) NOT NULL
);


ALTER TABLE users OWNER TO dbuser;

--
-- Data for Name: category; Type: TABLE DATA; Schema: public; Owner: dbuser
--

COPY category (categoryname) FROM stdin;
\.


--
-- Data for Name: photo; Type: TABLE DATA; Schema: public; Owner: dbuser
--

COPY photo (id, title, author, tags, essay) FROM stdin;
\.


--
-- Data for Name: photocomments; Type: TABLE DATA; Schema: public; Owner: dbuser
--

COPY photocomments (photoid, username, date, readercomment) FROM stdin;
\.


--
-- Data for Name: photodetail; Type: TABLE DATA; Schema: public; Owner: dbuser
--

COPY photodetail (photoid, datetaken, dateuploaded, location, camera, focallength, shutterspeed, aperture, iso, copyright) FROM stdin;
\.


--
-- Data for Name: photoxcategory; Type: TABLE DATA; Schema: public; Owner: dbuser
--

COPY photoxcategory (photoid, category) FROM stdin;
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: dbuser
--

COPY users (username, password) FROM stdin;
222	222
wkm	wkm
wkm1	wkm1
wkm12	wkm12
\.


--
-- Name: category_pkey; Type: CONSTRAINT; Schema: public; Owner: dbuser
--

ALTER TABLE ONLY category
    ADD CONSTRAINT category_pkey PRIMARY KEY (categoryname);


--
-- Name: photo_pkey; Type: CONSTRAINT; Schema: public; Owner: dbuser
--

ALTER TABLE ONLY photo
    ADD CONSTRAINT photo_pkey PRIMARY KEY (id);


--
-- Name: photodetail_pkey; Type: CONSTRAINT; Schema: public; Owner: dbuser
--

ALTER TABLE ONLY photodetail
    ADD CONSTRAINT photodetail_pkey PRIMARY KEY (photoid);


--
-- Name: users_pkey; Type: CONSTRAINT; Schema: public; Owner: dbuser
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_pkey PRIMARY KEY (username);


--
-- Name: photo_author_fkey; Type: FK CONSTRAINT; Schema: public; Owner: dbuser
--

ALTER TABLE ONLY photo
    ADD CONSTRAINT photo_author_fkey FOREIGN KEY (author) REFERENCES users(username);


--
-- Name: photocomments_photoid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: dbuser
--

ALTER TABLE ONLY photocomments
    ADD CONSTRAINT photocomments_photoid_fkey FOREIGN KEY (photoid) REFERENCES photo(id);


--
-- Name: photocomments_username_fkey; Type: FK CONSTRAINT; Schema: public; Owner: dbuser
--

ALTER TABLE ONLY photocomments
    ADD CONSTRAINT photocomments_username_fkey FOREIGN KEY (username) REFERENCES users(username);


--
-- Name: photodetail_photoid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: dbuser
--

ALTER TABLE ONLY photodetail
    ADD CONSTRAINT photodetail_photoid_fkey FOREIGN KEY (photoid) REFERENCES photo(id);


--
-- Name: photoxcategory_category_fkey; Type: FK CONSTRAINT; Schema: public; Owner: dbuser
--

ALTER TABLE ONLY photoxcategory
    ADD CONSTRAINT photoxcategory_category_fkey FOREIGN KEY (category) REFERENCES category(categoryname);


--
-- Name: photoxcategory_photoid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: dbuser
--

ALTER TABLE ONLY photoxcategory
    ADD CONSTRAINT photoxcategory_photoid_fkey FOREIGN KEY (photoid) REFERENCES photo(id);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

