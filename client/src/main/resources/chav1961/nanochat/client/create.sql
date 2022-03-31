CREATE TABLE cl_settings (
	settings_id INTEGER PRIMARY KEY,
	settings_uuid BLOB(16) NOT NULL,
	settings_name TEXT NOT NULL,
	settings_icon BLOB NOT NULL,
	settings_district TEXT NOT NULL,
	settings_lang_default TEXT NOT NULL,
	settings_use_en_in_tray INTEGER NOT NULL,
);

CREATE TABLE cl_discoveries (
	discoveries_id INTEGER PRIMARY KEY,
	settings_id INTEGER FOREIGN KEY cl_settings(s_id),
	discoveries_address TEXT NOT NULL,
	discoveries_is_available INTEGER NOT NULL
);

CREATE TABLE cl_options (
	options_id INTEGER PRIMARY KEY,
	settings_id INTEGER REFERENCES cl_settings(s_id),
	options_name TEXT NOT NULL,
	options_is_available INTEGER NOT NULL,
	options_is_available_now INTEGER NOT NULL
);

CREATE TABLE cl_contacts (
	contacts_id INTEGER PRIMARY KEY,
	contacts_name TEXT NOT NULL,
	contacts_type INTEGER NOT NULL,
	contacts_created INTEGER NOT NULL,
	contacts_state INTEGER NOT NULL,
	contacts_discovery_type INTEGER NOT NULL,
	contacts_connection BLOB NOT NULL
);

CREATE TABLE cl_bands (
	bands_id INTEGER PRIMARY KEY,
	bands_name TEXT NOT NULL,
	bands_password_sha BLOB(32) NOT NULL,
	bands_keystore BLOB NOT NULL,
	bands_is_owner INTEGER NOT NULL,
);

CREATE TABLE cl_band_members (
	members_id INTEGER PRIMARY KEY,
	bands_id REFERENCES cl_bands(bands_id),
	members_uuid BLOB(16) NOT NULL,
	members_name TEXT NOT NULL,
	members_created INTEGER NOT NULL,
	members_state INTEGER NOT NULL,
	members_salt INTEGER NOT NULL
);

CREATE TABLE cl_topics (
	topics_id INTEGER PRIMARY KEY,
	topics_initiator BLOB(16) NOT NULL,
	topics_theme TEXT NOT NULL,
	topics_created INTEGER NOT NULL,
	topics_last_modification INTEGER NOT NULL,
	topics_state INTEGER NOT NULL,
);

CREATE TABLE cl_topics_content (
	content_id INTEGER PRIMARY KEY,
	topics_id INTEGER REFERENCES cl_topics(topics_id),
	content_members_uuid BLOB(16) NOT NULL,
	content_created INTEGER NOT NULL,
	content_last_modification INTEGER NOT NULL,
	content_raw BLOB NOT NULL
);

CREATE TABLE cl_archive_topics (
	topics_id INTEGER PRIMARY KEY,
	topics_initiator BLOB(16) NOT NULL,
	topics_theme TEXT NOT NULL,
	topics_created INTEGER NOT NULL,
	topics_last_modification INTEGER NOT NULL,
	topics_state INTEGER NOT NULL,
);

CREATE TABLE cl_archive_topics_content (
	content_id INTEGER PRIMARY KEY,
	topics_id INTEGER REFERENCES cl_topics(topics_id),
	content_members_uuid BLOB(16) NOT NULL,
	content_created INTEGER NOT NULL,
	content_last_modification INTEGER NOT NULL,
	content_raw BLOB NOT NULL
);
