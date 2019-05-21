/**
 * @license Copyright (c) 2003-2019, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see https://ckeditor.com/legal/ckeditor-oss-license
 */

CKEDITOR.editorConfig = function( config ) {
	// Define changes to default configuration here.
	// For complete reference see:
	// https://ckeditor.com/docs/ckeditor4/latest/api/CKEDITOR_config.html

	// The toolbar groups arrangement, optimized for two toolbar rows.
	config.language = 'zh-cn';

	config.height = 400;

	config.extraPlugins = 'image2';

	config.filebrowserImageUploadUrl='/cms/content/uploadImage';

	config.toolbarGroups = [
		{name: 'document', groups: ['mode', 'document', 'doctools']},
		{name: 'tools', groups: ['tools']},
		{name: 'clipboard', groups: ['clipboard', 'undo']},
		{name: 'editing', groups: ['find', 'selection', 'spellchecker', 'editing']},
		{name: 'forms', groups: ['forms']},
		{name: 'basicstyles', groups: ['basicstyles', 'cleanup']},
		{name: 'colors', groups: ['colors']},
		{name: 'styles', groups: ['styles']},
		{name: 'paragraph', groups: ['list', 'indent', 'blocks', 'align', 'bidi', 'paragraph']},
		{name: 'others', groups: ['others']},
		{name: 'about', groups: ['about']},
		{name: 'links', groups: ['links']},
		{name: 'insert', groups: ['insert']}
	];

	// Remove some buttons provided by the standard plugins, which are
	// not needed in the Standard(s) toolbar.
	config.removeButtons = 'Underline,Subscript,Superscript';

	// Set the most common block elements.
	config.format_tags = 'p;h1;h2;h3;pre';

	// Simplify the dialog windows.
	config.removeDialogTabs = 'image:advanced;link:advanced';

	CKEDITOR.editorConfig = function( config ) {
		config.uiColor = '#F7B42C';
		config.height = 300;
		config.toolbarCanCollapse = true;
	};
};
