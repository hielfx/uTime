(function () {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('legal-entity-form', {
                parent: 'account',
                url: '/legalEntity/register',
                data: {
                    authorities: [],
                    pageTile: "register.title"
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/legal-entity-form/legal-entity-form-dialog.html',
                        controller: 'LegalEntityFormDialogController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('legalEntityForm');
                        $translatePartialLoader.addPart('global');
                        $translatePartialLoader.addPart('register');
                        return $translate.refresh();
                    }],
                    entity: function () {
                        return {
                            birthDate: null,
                            phoneNumber: null,
                            image: null,
                            imageContentType: null,
                            address: null,
                            city: null,
                            zipCode: null,
                            province: null,
                            country: null,
                            firstName: null,
                            lastName: null,
                            email: null,
                            password: null,
                            passwordConfirm: null,
                            login: null,
                            acceptTermsAndConditions: false,
                            mission: null,
                            vision: null,
                            website: null,
                            description: null
                        }
                    }
                }
            })
    }

})();
