(function () {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('natural-person-form', {
                parent: 'account',
                url: '/naturalPerson/register',
                data: {
                    authorities: [],
                    pageTile: "register.title"
                },
                views: {
                    'content@': {
                        templateUrl: 'app/entities/natural-person-form/natural-person-form-dialog.html',
                        controller: 'NaturalPersonFormDialogController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('naturalPersonForm');
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
                            acceptTermsAndConditions: false
                        }
                    }
                }
            })
    }

})();
