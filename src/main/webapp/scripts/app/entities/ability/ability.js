'use strict';

angular.module('volunteercrowdApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('ability', {
                parent: 'entity',
                url: '/abilitys',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'volunteercrowdApp.ability.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/ability/abilitys.html',
                        controller: 'AbilityController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('ability');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('ability.detail', {
                parent: 'entity',
                url: '/ability/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'volunteercrowdApp.ability.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/ability/ability-detail.html',
                        controller: 'AbilityDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('ability');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Ability', function($stateParams, Ability) {
                        return Ability.get({id : $stateParams.id});
                    }]
                }
            })
            .state('ability.new', {
                parent: 'ability',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/ability/ability-dialog.html',
                        controller: 'AbilityDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    hidden: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('ability', null, { reload: true });
                    }, function() {
                        $state.go('ability');
                    })
                }]
            })
            .state('ability.edit', {
                parent: 'ability',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/ability/ability-dialog.html',
                        controller: 'AbilityDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Ability', function(Ability) {
                                return Ability.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('ability', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('ability.delete', {
                parent: 'ability',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/ability/ability-delete-dialog.html',
                        controller: 'AbilityDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Ability', function(Ability) {
                                return Ability.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('ability', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
