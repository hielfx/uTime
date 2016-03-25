'use strict';

angular.module('volunteercrowdApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('legalEntity', {
                parent: 'entity',
                url: '/legalEntitys',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'volunteercrowdApp.legalEntity.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/legalEntity/legalEntitys.html',
                        controller: 'LegalEntityController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('legalEntity');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('legalEntity.detail', {
                parent: 'entity',
                url: '/legalEntity/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'volunteercrowdApp.legalEntity.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/legalEntity/legalEntity-detail.html',
                        controller: 'LegalEntityDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('legalEntity');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'LegalEntity', function($stateParams, LegalEntity) {
                        return LegalEntity.get({id : $stateParams.id});
                    }]
                }
            })
            .state('legalEntity.new', {
                parent: 'legalEntity',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/legalEntity/legalEntity-dialog.html',
                        controller: 'LegalEntityDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    mission: null,
                                    vision: null,
                                    website: null,
                                    description: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('legalEntity', null, { reload: true });
                    }, function() {
                        $state.go('legalEntity');
                    })
                }]
            })
            .state('legalEntity.edit', {
                parent: 'legalEntity',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/legalEntity/legalEntity-dialog.html',
                        controller: 'LegalEntityDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['LegalEntity', function(LegalEntity) {
                                return LegalEntity.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('legalEntity', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('legalEntity.delete', {
                parent: 'legalEntity',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/legalEntity/legalEntity-delete-dialog.html',
                        controller: 'LegalEntityDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['LegalEntity', function(LegalEntity) {
                                return LegalEntity.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('legalEntity', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
