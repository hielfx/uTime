'use strict';

angular.module('volunteercrowdApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('neededAbility', {
                parent: 'entity',
                url: '/neededAbilitys',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'volunteercrowdApp.neededAbility.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/neededAbility/neededAbilitys.html',
                        controller: 'NeededAbilityController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('neededAbility');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('neededAbility.detail', {
                parent: 'entity',
                url: '/neededAbility/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'volunteercrowdApp.neededAbility.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/neededAbility/neededAbility-detail.html',
                        controller: 'NeededAbilityDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('neededAbility');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'NeededAbility', function($stateParams, NeededAbility) {
                        return NeededAbility.get({id : $stateParams.id});
                    }]
                }
            })
            .state('neededAbility.new', {
                parent: 'neededAbility',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/neededAbility/neededAbility-dialog.html',
                        controller: 'NeededAbilityDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('neededAbility', null, { reload: true });
                    }, function() {
                        $state.go('neededAbility');
                    })
                }]
            })
            .state('neededAbility.edit', {
                parent: 'neededAbility',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/neededAbility/neededAbility-dialog.html',
                        controller: 'NeededAbilityDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['NeededAbility', function(NeededAbility) {
                                return NeededAbility.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('neededAbility', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('neededAbility.delete', {
                parent: 'neededAbility',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/neededAbility/neededAbility-delete-dialog.html',
                        controller: 'NeededAbilityDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['NeededAbility', function(NeededAbility) {
                                return NeededAbility.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('neededAbility', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
