'use strict';

angular.module('volunteercrowdApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('need', {
                parent: 'entity',
                url: '/needs',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'volunteercrowdApp.need.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/need/needs.html',
                        controller: 'NeedController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('need');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('need.detail', {
                parent: 'entity',
                url: '/need/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'volunteercrowdApp.need.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/need/need-detail.html',
                        controller: 'NeedDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('need');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Need', function($stateParams, Need) {
                        return Need.get({id : $stateParams.id});
                    }]
                }
            })
            .state('need.new', {
                parent: 'need',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/need/need-dialog.html',
                        controller: 'NeedDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    title: null,
                                    description: null,
                                    cathegory: null,
                                    deleted: null,
                                    location: null,
                                    creationDate: null,
                                    modificationDate: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('need', null, { reload: true });
                    }, function() {
                        $state.go('need');
                    })
                }]
            })
            .state('need.edit', {
                parent: 'need',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/need/need-dialog.html',
                        controller: 'NeedDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Need', function(Need) {
                                return Need.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('need', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('need.delete', {
                parent: 'need',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/need/need-delete-dialog.html',
                        controller: 'NeedDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Need', function(Need) {
                                return Need.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('need', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
