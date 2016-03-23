'use strict';

angular.module('volunteercrowdApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('request', {
                parent: 'entity',
                url: '/requests',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'volunteercrowdApp.request.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/request/requests.html',
                        controller: 'RequestController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('request');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('request.detail', {
                parent: 'entity',
                url: '/request/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'volunteercrowdApp.request.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/request/request-detail.html',
                        controller: 'RequestDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('request');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Request', function($stateParams, Request) {
                        return Request.get({id : $stateParams.id});
                    }]
                }
            })
            .state('request.new', {
                parent: 'request',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/request/request-dialog.html',
                        controller: 'RequestDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    creationDate: null,
                                    description: null,
                                    code: null,
                                    finishDate: null,
                                    deleted: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('request', null, { reload: true });
                    }, function() {
                        $state.go('request');
                    })
                }]
            })
            .state('request.edit', {
                parent: 'request',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/request/request-dialog.html',
                        controller: 'RequestDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Request', function(Request) {
                                return Request.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('request', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('request.delete', {
                parent: 'request',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/request/request-delete-dialog.html',
                        controller: 'RequestDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Request', function(Request) {
                                return Request.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('request', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
