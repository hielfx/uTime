'use strict';

angular.module('volunteercrowdApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('naturalPerson', {
                parent: 'entity',
                url: '/naturalPersons',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'volunteercrowdApp.naturalPerson.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/naturalPerson/naturalPersons.html',
                        controller: 'NaturalPersonController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('naturalPerson');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('naturalPerson.detail', {
                parent: 'entity',
                url: '/naturalPerson/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'volunteercrowdApp.naturalPerson.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/naturalPerson/naturalPerson-detail.html',
                        controller: 'NaturalPersonDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('naturalPerson');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'NaturalPerson', function($stateParams, NaturalPerson) {
                        return NaturalPerson.get({id : $stateParams.id});
                    }]
                }
            })
            .state('naturalPerson.new', {
                parent: 'naturalPerson',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/naturalPerson/naturalPerson-dialog.html',
                        controller: 'NaturalPersonDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    birthDate: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('naturalPerson', null, { reload: true });
                    }, function() {
                        $state.go('naturalPerson');
                    })
                }]
            })
            .state('naturalPerson.edit', {
                parent: 'naturalPerson',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/naturalPerson/naturalPerson-dialog.html',
                        controller: 'NaturalPersonDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['NaturalPerson', function(NaturalPerson) {
                                return NaturalPerson.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('naturalPerson', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('naturalPerson.delete', {
                parent: 'naturalPerson',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/naturalPerson/naturalPerson-delete-dialog.html',
                        controller: 'NaturalPersonDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['NaturalPerson', function(NaturalPerson) {
                                return NaturalPerson.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('naturalPerson', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
